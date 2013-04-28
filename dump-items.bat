@echo off
@title Dump
set CLASSPATH=.;dist\*
java -server -Dnet.sf.odinms.wzpath=wz tools.wztosql.DumpItems
pause