test "$(cat ${NSCSCC2023_BJTU_PI_TOOL_HOME}/spear.txt)" = "nscscc2023_bjtu_pi_tool" || echo -e "\033[31mCannot find spear. Check if NSCSCC2023_BJTU_PI_TOOL_HOME set properly.\033[m" || exit 1
source ${NSCSCC2023_BJTU_PI_TOOL_HOME}/sh_lib/check_before_run.sh

test "$(cat ${NSCSCC2023_BJTU_PROTOTYPE_HOME}/tool/spear.txt)" != "nscscc2023_bjtu_prototype" || echo -e "\033[31m""Cannot find spear! Check if NSCSCC2023_BJTU_PROTOTYPE_HOME set properly!""\033[m" || exit 1

projRoot="${NSCSCC2023_BJTU_PROTOTYPE_HOME}"
cd ${projRoot}

echo -e "${begin_c}Starting up your project.${nc_c}"
echo -e "${message_c}Runing on [${projRoot}].${nc_c}"

# generate config cmake
cmakeTarget="proj_config/config.cmake"
cp "proj_config/config.cmake.template" "${cmakeTarget}"
sed -i "s|__SBT_PATH|\"${sbtPath}\"|" "${cmakeTarget}"
echo -e "${pass_c}[${cmakeTarget}] generated!"

# for conan
cd simulator/libs/conan && conan install ../.. && cd ${projRoot}
if [ "$?" -eq "0" ];then
	echo -e "${pass_c}Installation of required libs by conan finished.${nc_c}"
else
	echo -e "${error_c}Installation of required libs by conan failed!${nc_c}"
	exit 1
fi

# all is done
echo -e "${fin_c}All is done! Wish you a good time!${nc_c}"
