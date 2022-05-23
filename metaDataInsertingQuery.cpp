#include <iostream>
#include <fstream>
#include <string>
#include <sstream>

int main (int ag, char** av)
{
	std::ifstream fin(av[1]);
	std::string line;
	std::ofstream fo(av[2]);
	std::string query = "insert into post (created_date, modified_date, content, member_id, thumbnail_file_name) values (";
	while(std::getline(fin, line)){
		std::string row = "(";
		std::string token;
		std::stringstream sstream(line);
		std::getline(sstream, token, '|');
		row += token;
		while (std::getline(sstream, token, '|')){
			if (row.length() != 1)
				row += ",";
			std::string line = row;
			row.erase(line.find_first_of(" "), 1);
			std::cout << row << std::endl;
			// row.erase(row.end());
			row += token;
		}
		row += ") ";
		fo << row;
	}
	fin.close();
	fo.close();
}	
