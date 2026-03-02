#!/bin/bash

# ============================================
# Distributed AgentScope 快速启动脚本 (Linux/Mac)
# ============================================

set -e

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 日志函数
log_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

log_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

log_warn() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# 检查 Docker 是否安装
check_docker() {
    if ! command -v docker &> /dev/null; then
        log_error "Docker 未安装，请先安装 Docker"
        exit 1
    fi
    
    if ! command -v docker-compose &> /dev/null; then
        log_error "Docker Compose 未安装，请先安装 Docker Compose"
        exit 1
    fi
    
    log_success "Docker 和 Docker Compose 已安装"
}

# 检查 Docker 是否运行
check_docker_running() {
    if ! docker info &> /dev/null; then
        log_error "Docker 未运行，请启动 Docker"
        exit 1
    fi
    log_success "Docker 正在运行"
}

# 等待服务就绪
wait_for_service() {
    local service=$1
    local max_attempts=$2
    local delay=$3
    local attempt=1
    
    log_info "等待 $service 服务就绪..."
    
    while [ $attempt -le $max_attempts ]; do
        if docker-compose ps "$service" | grep -q "Up"; then
            log_success "$service 服务已就绪"
            return 0
        fi
        echo -ne "\r等待中... ($attempt/$max_attempts)"
        sleep $delay
        attempt=$((attempt + 1))
    done
    
    echo ""
    log_error "$service 服务启动超时"
    return 1
}

# 等待 MongoDB 就绪
wait_for_mongodb() {
    log_info "等待 MongoDB 就绪..."
    local max_attempts=30
    local attempt=1
    
    while [ $attempt -le $max_attempts ]; do
        if docker exec agentscope-mongodb mongosh --eval "db.runCommand('ping')" &> /dev/null; then
            log_success "MongoDB 已就绪"
            return 0
        fi
        echo -ne "\r等待 MongoDB 启动... ($attempt/$max_attempts)"
        sleep 2
        attempt=$((attempt + 1))
    done
    
    echo ""
    log_error "MongoDB 启动超时"
    return 1
}

# 启动所有服务
start_all() {
    log_info "正在启动所有服务..."
    log_info "启动顺序：MongoDB -> Agent Server -> Agent Client"
    
    # 启动所有服务（docker-compose 会自动处理依赖）
    docker-compose up -d
    
    # 等待 MongoDB 就绪
    wait_for_mongodb
    
    # 等待 Server 就绪
    wait_for_service "agent-server" 30 2
    
    # 等待 Client 就绪
    wait_for_service "agent-client" 30 2
    
    log_success "所有服务已启动"
    echo ""
    echo "============================================"
    echo "  服务访问地址:"
    echo "    Agent Server: http://localhost:8080"
    echo "    Agent Client: http://localhost:8081"
    echo "    MongoDB:      localhost:27017"
    echo "============================================"
    echo ""
    echo "查看日志：./scripts/start.sh logs"
    echo "停止服务：./scripts/start.sh stop"
}

# 停止所有服务
stop_all() {
    log_info "正在停止所有服务..."
    docker-compose down
    log_success "所有服务已停止"
}

# 重启所有服务
restart_all() {
    log_info "正在重启所有服务..."
    docker-compose restart
    log_success "所有服务已重启"
}

# 查看日志
view_logs() {
    local service=$1
    if [ -z "$service" ]; then
        log_info "查看所有服务日志..."
        docker-compose logs -f
    else
        log_info "查看 $service 服务日志..."
        docker-compose logs -f "$service"
    fi
}

# 构建镜像
build_images() {
    log_info "正在构建 Docker 镜像..."
    docker-compose build
    log_success "镜像构建完成"
}

# 清理资源
clean() {
    log_warn "警告：此操作将删除所有容器、网络和卷！"
    read -p "确定要继续吗？(y/N): " confirm
    if [ "$confirm" = "y" ] || [ "$confirm" = "Y" ]; then
        docker-compose down -v
        log_success "资源已清理"
    else
        log_info "操作已取消"
    fi
}

# 显示状态
status() {
    log_info "服务状态："
    docker-compose ps
}

# 显示帮助
show_help() {
    echo "用法：$0 [命令]"
    echo ""
    echo "命令:"
    echo "  start       启动所有服务 (MongoDB + Server + Client)"
    echo "  start-deps  只启动依赖服务 (MongoDB)"
    echo "  stop        停止所有服务"
    echo "  restart     重启所有服务"
    echo "  build       构建 Docker 镜像"
    echo "  logs        查看日志 (可指定服务名)"
    echo "  status      查看服务状态"
    echo "  clean       清理所有资源"
    echo "  help        显示帮助信息"
    echo ""
    echo "示例:"
    echo "  $0 start           # 启动所有服务"
    echo "  $0 start-deps      # 只启动 MongoDB"
    echo "  $0 logs            # 查看所有日志"
    echo "  $0 logs agent-server  # 查看 server 日志"
    echo ""
    echo "服务端口:"
    echo "  Agent Server: http://localhost:8080"
    echo "  Agent Client: http://localhost:8081"
    echo "  MongoDB:      localhost:27017"
}

# 主函数
main() {
    case "$1" in
        start)
            check_docker
            check_docker_running
            start_all
            ;;
        start-deps)
            check_docker
            check_docker_running
            log_info "正在启动依赖服务 (MongoDB)..."
            docker-compose up -d mongodb
            wait_for_mongodb
            log_success "依赖服务已启动"
            ;;
        stop)
            check_docker
            stop_all
            ;;
        restart)
            check_docker
            restart_all
            ;;
        build)
            check_docker
            build_images
            ;;
        logs)
            view_logs "$2"
            ;;
        status)
            check_docker
            status
            ;;
        clean)
            check_docker
            clean
            ;;
        help|--help|-h)
            show_help
            ;;
        *)
            show_help
            ;;
    esac
}

main "$@"
