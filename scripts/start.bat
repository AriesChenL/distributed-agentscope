@echo off
chcp 65001 >nul

:: ============================================
:: Distributed AgentScope 快速启动脚本 (Windows)
:: ============================================

setlocal enabledelayedexpansion

:menu
echo.
echo ============================================
echo   Distributed AgentScope 快速启动菜单
echo ============================================
echo.
echo   1. 启动所有服务
echo   2. 停止所有服务
echo   3. 重启所有服务
echo   4. 构建 Docker 镜像
echo   5. 查看日志
echo   6. 查看服务状态
echo   7. 清理所有资源
echo   8. 退出
echo.

set /p choice="请选择操作 (1-8): "

if "%choice%"=="1" goto start
if "%choice%"=="2" goto stop
if "%choice%"=="3" goto restart
if "%choice%"=="4" goto build
if "%choice%"=="5" goto logs
if "%choice%"=="6" goto status
if "%choice%"=="7" goto clean
if "%choice%"=="8" goto end

echo 无效的选择，请重新输入
goto menu

:start
echo.
echo [INFO] 正在启动所有服务...
docker-compose up -d
if %errorlevel% neq 0 (
    echo [ERROR] 启动失败
) else (
    echo [SUCCESS] 所有服务已启动
    echo.
    echo 服务访问地址:
    echo   Agent Server: http://localhost:8080
    echo   Agent Client: http://localhost:8081
    echo   MongoDB:      localhost:27017
)
echo.
pause
goto menu

:stop
echo.
echo [INFO] 正在停止所有服务...
docker-compose down
if %errorlevel% neq 0 (
    echo [ERROR] 停止失败
) else (
    echo [SUCCESS] 所有服务已停止
)
echo.
pause
goto menu

:restart
echo.
echo [INFO] 正在重启所有服务...
docker-compose restart
if %errorlevel% neq 0 (
    echo [ERROR] 重启失败
) else (
    echo [SUCCESS] 所有服务已重启
)
echo.
pause
goto menu

:build
echo.
echo [INFO] 正在构建 Docker 镜像...
docker-compose build
if %errorlevel% neq 0 (
    echo [ERROR] 构建失败
) else (
    echo [SUCCESS] 镜像构建完成
)
echo.
pause
goto menu

:logs
echo.
echo [INFO] 查看日志...
echo 选择要查看的服务:
echo   1. agent-server
echo   2. agent-client
echo   3. mongodb
echo   4. 所有服务
echo.
set /p log_choice="请选择 (1-4): "
if "%log_choice%"=="1" docker-compose logs -f agent-server
if "%log_choice%"=="2" docker-compose logs -f agent-client
if "%log_choice%"=="3" docker-compose logs -f mongodb
if "%log_choice%"=="4" docker-compose logs -f
goto menu

:status
echo.
echo [INFO] 服务状态:
docker-compose ps
echo.
pause
goto menu

:clean
echo.
echo [WARN] 警告：此操作将删除所有容器、网络和卷!
set /p confirm="确定要继续吗？(y/N): "
if /i "%confirm%"=="y" (
    docker-compose down -v
    if %errorlevel% neq 0 (
        echo [ERROR] 清理失败
    ) else (
        echo [SUCCESS] 资源已清理
    )
) else (
    echo [INFO] 操作已取消
)
echo.
pause
goto menu

:end
echo.
echo [INFO] 再见!
echo.
exit /b
