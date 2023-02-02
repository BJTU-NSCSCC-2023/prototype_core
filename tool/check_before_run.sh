if [ "$(cat ${NSCSCC2023_BJTU_PROTOTYPE_HOME}/tool/spear_point.txt)" != "spear_for_prototype" ];then
   echo -e "\033[31m""Cannot find spear! Check if NSCSCC2023_BJTU_PROTOTYPE_HOME set properly!""\033[m"
   exit 1
fi

projRoot="${NSCSCC2023_BJTU_PROTOTYPE_HOME}"
cd ${projRoot}

source tool/lib/decorator.sh

echo -e "${begin_c}Checking your running env...${nc_c}"

condaVersion="$(conda --version)"
# echo -e "${message_c}Finding conda..."
if [[ "${condaVersion}" =~ "conda *" ]];then
	echo -e "${pass_c}conda found! Version: [${condaVersion}]${nc_c}"
else
	echo -e "${error_c}Can't find conda!${nc_c}"
	exit 1
fi

# echo -e "${message_c}Finding conda env nscscc2023-bjtu-pyenv...${nc_c}"
pyEnvInfo="$(conda env list | grep nscscc2023-bjtu-pyenv)"
if [ -n "${pyEnvInfo}" ];then
	echo -e "${pass_c}Found nscscc2023-bjtu-pyenv on [${pyEnvInfo}].${nc_c}"
else
	echo -e "${error_c}Can't find nscscc2023-bjtu-pyenv !${nc_c}"
	exit 1
fi

echo -e "${fin_c}Check all passed!${nc_c}"

py="conda run -n nscscc2023-bjtu-pyenv python3"
