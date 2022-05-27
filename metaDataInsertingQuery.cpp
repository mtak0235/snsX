#include <iostream>
#include <fstream>
#include <string>
#include <sstream>
#include <vector>

using namespace std;
std::vector<std::string> ft_split(std::string input, char delimiter);

int main(int ag, char **av)
{
	std::ifstream fin("data.txt");
	std::string line;
	std::ofstream fo("sql.txt");
	std::string query = "insert into post (created_date, modified_date, content, member_id, thumbnail_file_name) values ";
	fo << query;
	std::string ss;
	while (std::getline(fin, line))
	{
		std::string row = "(";
		std::vector<std::string> res = ft_split(line, '|');
		res.erase(res.begin());
		for (int i = 0; i < res.size(); i++)
		{
			if (i != 0)
				row += ",";
			std::string::size_type begin = res[i].find_first_not_of(' ');
			std::string::size_type end = res[i].find_last_not_of(' ');
			std::string trimmed = res[i].substr(begin, end - begin + 1);
			row += ('\'' + trimmed + '\'');
		}
		row += "),";
		ss += row;
	}
	for (int i = 0; i < 10000; i++)
	{
		fo << ss;
	}
	fo << ";";
	fin.close();
	fo.close();
}

std::vector<std::string> ft_split(std::string input, char delimiter)
{
	std::vector<std::string> ans;
	stringstream ss(input);
	string tmp;
	while (getline(ss, tmp, delimiter))
	{
		ans.push_back(tmp);
	}
	return ans;
}