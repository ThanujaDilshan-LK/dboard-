#!/bin/sh
app_path=$0
while [ -h "$app_path" ]; do
    ls=`ls -ld "$app_path"`
    link=`expr "$ls" : '.*-> \(.*\)$'`
    if expr "$link" : '/.*' > /dev/null; then
        app_path="$link"
    else
        app_path=`dirname "$app_path"`/"$link"
    fi
done
APP_HOME=`dirname "$app_path"`
APP_BASE_NAME=`basename "$app_path"`
unset CDPATH
DEFAULT_JVM_OPTS='"-Xmx64m" "-Xms64m"'
MAX_FD=maximum
warn () { echo "$*"; }
die () { echo; echo "$*"; echo; exit 1; }
cygwin=false; msys=false; darwin=false; nonstop=false
case "`uname`" in
  CYGWIN* ) cygwin=true ;;
  Darwin* ) darwin=true ;;
  MSYS* | MINGW* ) msys=true ;;
  NONSTOP* ) nonstop=true ;;
esac
if [ "$JAVA_HOME" ] ; then
    JAVACMD="$JAVA_HOME/bin/java"
else
    JAVACMD="java"
fi
exec "$JAVACMD" "-Dorg.gradle.appname=$APP_BASE_NAME" -classpath "$APP_HOME/gradle/wrapper/gradle-wrapper.jar" org.gradle.wrapper.GradleWrapperMain "$@"
