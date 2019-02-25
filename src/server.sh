#!/bin/sh

#运行
run()
{
	export JAVA_HOME=/data/jdk1.8.0_65
	export PATH=$PATH:$JAVA_HOME/bin
	export CLASSPATH=.:$JAVA_HOME/lib/tools.jar:$JAVA_HOME/lib/dt.jar

	#extend
	for i in ./lib/*.jar; do
		   CLASSPATH="$CLASSPATH":"$i"  
	done

	exec java -DrunDir=$runDir -DserverPort=$runPort -Duser.timezone=GMT+8 -Dfile.encoding=UTF-8 -Xmx128m -XX:-HeapDumpOnOutOfMemoryError $main
}

#停止
die()
{
	pid=$(ps -ef|grep "$runDir" | grep $main | grep -v 'grep' | awk '{print $2}')

	if [ -z "$pid" ];then
		echo "##################################################"
		echo "##$runDir/$main is close"
		echo "##################################################"
	else
		kill -TERM $pid
		echo "##################################################"
		echo "##close pid:"$pid
		echo "##################################################"
	fi
}

#运行目录
runDir=$(dirname "$0")

#修改目录权限
chmod -R 775 $runDir

#切换至工作目录
cd $runDir

#启动文件
main=com.web.boot.Startup

#端口
if [ -z "$2" ];then
	runPort=20000
else
	runPort=$2
fi

echo "##################################################"
echo "##runDir":$runDir
echo "##runPort":$runPort
echo "##runMain":$main
echo "##################################################"

if [ "$1" = "start" ]
then
	run
elif [ "$1" = "stop" ]
then
	die
else
	echo "params start|stop"
fi