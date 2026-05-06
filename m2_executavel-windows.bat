@echo off
cd /d "%~dp0"
echo Verificando Java...
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo Java nao encontrado!
    echo Baixe e instale em: https://www.java.com/download
    pause
    exit
)

echo Compilando...
if not exist "target" mkdir target
dir /s /b src\main\java\*.java > sources.txt
javac -d target @sources.txt
del sources.txt

if %errorlevel% neq 0 (
    echo Erro ao compilar!
    pause
    exit
)

echo Iniciando programa...
java -cp target br.com.m2_linguagens_formais.Main
pause