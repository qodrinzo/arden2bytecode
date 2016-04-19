@echo off
set PWD=%~dp0
java -cp "%PWD%bin;%PWD%lib\jewelcli-0.8.9.jar" arden.MainClass %*
pause
