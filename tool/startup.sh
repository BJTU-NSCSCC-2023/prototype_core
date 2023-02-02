if [ "$(cat ${NSCSCC2023_BJTU_PROTOTYPE_HOME}/tool/spear_point.txt)" != "spear_for_prototype" ];then
   echo -e "\033[31m""Cannot find spear! Check if NSCSCC2023_BJTU_PROTOTYPE_HOME set properly!""\033[m"
   exit 1
fi

projRoot="${NSCSCC2023_BJTU_PROTOTYPE_HOME}"
cd ${projRoot}

source tool/lib/decorator.sh


echo -e "${begin_c}Starting up your project.${nc_c}"
echo -e "${message_c}Runing on [${projRoot}].${nc_c}"

function find_bin(){
	local binName=$1
	# echo -e "${message_c}Finding ${binName}...${nc_c}"
	binPath="$(which "${binName}")"
	if [ ! -x "${binPath}" ];then
	   echo -e "${error_c}Cannot find executable ${binName}! Output of \`which ${binName}\`: [${binPath}]${nc_c}"
	   exit 1
	else
	   echo -e "${pass_c}Found ${binName} on [${binPath}]!${nc_c}"
	fi
	export ${binName}Path=${binPath}
}

# find sbt conan
find_bin sbt
find_bin conan
find_bin conda

# generate config cmake
# echo -e "${message_c}Generating[${cmakeTarget}]...${nc_c}"
cmakeTarget="proj_config/config.cmake"
cp "proj_config/config.cmake.template" "${cmakeTarget}"
sed -i "s|__SBT_PATH|\"${sbtPath}\"|" "${cmakeTarget}"
echo -e "${pass_c}[${cmakeTarget}] generated!"

# for conan
# echo -e "${message_c}Installing conan cmake..."
cd simulator/libs/conan && conan install ../.. && cd ${projRoot}
if [ "$?" -eq "0" ];then
	echo -e "${pass_c}Installation of required libs by conan finished.${nc_c}"
else
	echo -e "${error_c}Installation of required libs by conan failed!${nc_c}"
	exit 1
fi

# for conda env
# echo -e "${message_c}Installing conda pyenv...${nc_c}"
cd ${projRoot}
if [ ! -f tool/nscscc2023-bjtu-pyenv.yaml ];then
	echo -e "${error_c}Can't find pyenv yaml file.${nc_c}"
	exit 1
fi
if [ -n "$(conda env list | grep nscscc2023-bjtu-pyenv)" ];then
	echo -e "${message_c}Found nscscc2023-bjtu-pyenv conda env, updating it...${nc_c}"
	conda env update -n nscscc2023-bjtu-pyenv -f tool/nscscc2023-bjtu-pyenv.yaml
else
	echo -e "${message_c}Can't find nscscc2023-bjtu-pyenv conda env, creating it...${nc_c}"
	conda env create -n nscscc2023-bjtu-pyenv -f tool/nscscc2023-bjtu-pyenv.yaml
fi
if [ "$?" -ne "0" ];then
	echo "${error_c}conda env create/update failed.${nc_c}"
	exit 1
fi

# all is done
echo -e "${fin_c}All is done! Wish you a good time!${nc_c}"
