 if [ "$(cat tool/spear_point.txt)" != "spear_for_prototype" ];then
   echo -e "\033[31m""Cannot find spear! Run this script on project root!""\033[m"
   exit 1
fi

projRoot=$(pwd)

source tool/lib/decorator.sh
source tool/check_before_run.sh

echo -e "${begin_c}Building hex inst...${nc_c}"

converter=tool/mips-converter/converter.py
if [ ! -f "${converter}" ];then
    echo -e "${error_c}Converter not found in [${converter}]! Check if you update submodule on git.${nc_c}"
    exit 1
fi

echo -e "${message_c}Start building hex-inst...${nc_c}"
echo -e "${message_c}Runing on [${projRoot}].${nc_c}"

for srcFile in $(find data_set/hex_inst -name "*.inst")
do
    dstFile="${srcFile%.*}.hex"
    echo -e "${message_c}Generating [${dstFile}] from [${srcFile}]...${nc_c}"
	{ verbose="$(${py} ${converter} "${srcFile}" -o "${dstFile}" --func i2h --verbose)"; } 2> /dev/null
	if [ "$?" -ne 0 ];then
		echo -e "${error_c}Generate [${dstFile}] from [${srcFile}] failed!${nc_c}"
		echo -e "${error_c}Verbose info:${nc_c}"
		echo "${verbose}"
		exit 1
	fi
done

echo -e "${fin_c}Build finished.${nc_c}"
