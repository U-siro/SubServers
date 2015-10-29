if [ -z "$1" ]
  then
    echo ERROR: No Build Version Supplied
    rm -Rf build-subserver.sh
    exit 1
fi
if [ -z "$2" ]
    then
    echo ERROR: No Server Software Supplied
    rm -Rf build-subserver.sh
    exit 1
fi
echo ---------- SERVER BUILD START ----------
if [[ $2 == "bukkit" || $2 == "spigot" ]]
    then
    echo Downloading Buildtools...
    curl -o BuildTools.jar https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar; retvalb=$?
    if [ $retvalb -eq 0 ]; then
        echo Downloaded Buildtools!
    else
        echo ERROR: Failed Downloading Buildtools. Is SpigotMC.org down?
        rm -Rf build-subserver.sh
        exit 1
    fi
    if [ -d "Buildtools" ]
    then
        rm -Rf BuildTools
    fi
    mkdir Buildtools
    cd "BuildTools"
    echo Building CraftBukkit/Spigot Jarfiles...
    export MAVEN_OPTS="-Xmx2G"
    java -Xmx2G -jar ../BuildTools.jar --rev $1; retvalc=$?
    cd ../
    if [ $retvalc -eq 0 ]; then
        echo CraftBukkit/Spigot Jarfiles Built!
        if [ $2 == "spigot" ]; then
            cp Buildtools/spigot-$1.jar Spigot.jar
        else
            cp Buildtools/craftbukkit-$1.jar Craftbukkit.jar
        fi
        echo Added Jarfiles!
        echo Cleaning Up...
        rm -Rf BuildTools.jar
        rm -Rf BuildTools
        echo ---------- END SERVER BUILD ----------
        rm -Rf build-subserver.sh
        exit 0
    else
        ERROR: Buildtools exited with an error. Please try again
        rm -Rf BuildTools.jar
        rm -Rf BuildTools
        rm -Rf build-subserver.sh
        exit 1
    fi
else
    if [ $2 == "vanilla" ]; then
        if [ -d "Buildtools" ]
        then
            rm -Rf BuildTools
        fi
        mkdir Buildtools
        mkdir Buildtools/Vanilla
        echo Downloading Vanilla Server Jarfile
        curl -o Buildtools/Vanilla/minecraft_server.$1.jar https://s3.amazonaws.com/Minecraft.Download/versions/$1/minecraft_server.$1.jar; retvald=$?
        if [ $retvald -eq 0 ]; then
            echo Downloading Vanilla Patches...
            curl -o Buildtools/Vanilla/bungee-patch.jar http://minecraft.me1312.net/lib/subservers/vanilla-bungee-patch.1.2.jar; retvale=$?
            if [ $retvale -eq 0 ]; then
                echo Patching Vanilla for BungeeCord Support
                cd Buildtools/Vanilla
                java -jar bungee-patch.jar $1; retvalf=$?;
                if [ $retvalf -eq 0 ]; then
                    echo Patched Vanilla Jar!
                    cd ../../
                    cp Buildtools/Vanilla/out/$1-bungee.jar Buildtools/vanilla-$1.jar
                    cp Buildtools/Vanilla/out/$1-bungee.jar Vanilla.jar
                    echo Added Jarfiles!
                    echo Cleaning Up...
                    rm -Rf BuildTools
                    echo ---------- END SERVER BUILD ----------
                    rm -Rf build-subserver.sh
                    exit 0
                else
                    echo ERROR: Failed Applying Patch.
                    rm -Rf BuildTools
                    rm -Rf build-subserver.sh
                    exit 1
                fi
            else
                echo ERROR: Failed Downloading Patch. Is ME1312.net down?
                rm -Rf BuildTools
                rm -Rf build-subserver.sh
                exit 1
            fi
        else
            echo ERROR: Failed Downloading Jarfile. Is Minecraft.net down?
            rm -Rf BuildTools
            rm -Rf build-subserver.sh
            exit 1
        fi
    fi
fi