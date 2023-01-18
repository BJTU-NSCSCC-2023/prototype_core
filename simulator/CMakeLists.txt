cmake_minimum_required(VERSION 3.22)
project(Simulator)

#	c++ standard
set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED true)

#	c++ compile options
add_compile_options(
	#	-g -Wall -Werror -Wextra -Wno-unused-parameter
	-g -Wall -Wextra -Wno-unused-parameter
)

#	include config
include(${CMAKE_CURRENT_SOURCE_DIR}/../proj_config/config.cmake)

#	check: sbt
if (NOT SBT_PATH)
	message(FATAL_ERROR "Install sbt and run tool/startup.sh on project root first!")
endif ()

#	check: verilator
find_package(verilator HINTS $ENV{VERILATOR_ROOT} ${VERILATOR_ROOT})
if (NOT verilator_FOUND)
	message(FATAL_ERROR "Verilator was not found. Either install it, or set the VERILATOR_ROOT environment variable")
endif ()

#	load externer library envolved by conan (${CONAN_LIBS})
include(${CMAKE_CURRENT_SOURCE_DIR}/libs/conan/conanbuildinfo.cmake)
conan_basic_setup()

#	load externer library : moe
add_subdirectory(libs/moe)
include_directories(libs/moe)
link_libraries(moe ${CONAN_LIBS})

#	executable and subdirectories
add_subdirectory(sub_projects/sim)
