#include <iostream>
#include <boost/log/trivial.hpp>


int main(int, char **) {
	std::cout << "Hello" << std::endl;
	BOOST_LOG_TRIVIAL(info) << "Hello";
	return 0;
}
