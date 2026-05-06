#!/bin/bash
cd "$(dirname "$0")"

if ! command -v java &> /dev/null; then
    echo "Java nao encontrado! Baixe em: https://www.java.com/download"
    exit 1
fi

mkdir -p target
find src/main/java -name "*.java" > sources.txt
javac -d target @sources.txt
rm sources.txt

if [ $? -ne 0 ]; then
    echo "Erro ao compilar!"
    exit 1
fi

java -cp target br.com.m2_linguagens_formais.Main