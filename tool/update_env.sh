if [ "$(cat ${NSCSCC2023_BJTU_PROTOTYPE_HOME}/tool/spear_point.txt)" != "spear_for_prototype" ];then
   echo -e "\033[31m""Cannot find spear! Check if NSCSCC2023_BJTU_PROTOTYPE_HOME set properly!""\033[m"
   exit 1
fi

projRoot="${NSCSCC2023_BJTU_PROTOTYPE_HOME}"
cd ${projRoot}
source tool/check_before_run.sh

echo -e "${begin_c}Updating your env...${nc_c}"

echo -e "${message_c}Dumping conda env yaml.${nc_c}"
conda env export -n nscscc2023-bjtu-pyenv --file tool/nscscc2023-bjtu-pyenv.yaml

echo -e "${message_c}Updating submodules recursive...${nc_c}"
git submodule update --recursive

echo -e "${fin_c}Finished.${nc_c}"
