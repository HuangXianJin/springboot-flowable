@echo off
cd /d %~dp0
copy %~dp0env\light.env %~dp0.env
npm run dev