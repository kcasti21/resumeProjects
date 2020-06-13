#include "Movie.h"
#include "Review.h"
#include <iostream>
#include <string>
#include <vector>
#include <algorithm>
#include <numeric>

using std::string;
using std::vector;
using std::stringstream;
using std::getline;
using std::sort;
using std::cout;
using std::stoi;
using std::endl;


Movie:: Movie(int ID, string name, int year, double rating, int quant)
				: movieName(name), movieID(ID), year(year), avgRating(rating), quantity(quant)
					{
						for (int i = 0; i < 5; i++)
						{
							ratings[i] = 0;
						}
					}

	void Movie::setArray()
	{
		for (int i = 0; i < 5; i++)
		{
			ratings[i] = 0;
		}
	}

	void Movie:: update(int i)
	{
		ratings[i - 1]++;
	}
 int Movie::calcAverage(const vector<Review>& reviews, int index)
	{
		//setArray();
		int count = 0;
		int ID = reviews[index].movieID;
		for (auto i = reviews.begin() + index; i != reviews.end() - 1; ++i)
		{
			if (ID == (*i).movieID)
			{
				avgRating += (*i).rating;
				update((*i).rating);
				count++;
			}
			else
			{
				if (count != 0)
					avgRating = avgRating / count;
				quantity = count;
				return index + count;
			}
		}
		if (count != 0)
			avgRating = avgRating / count;
	  quantity = count;
		return count + index;
	}
	void Movie:: print()
	{
		cout << movieID << "	" << quantity << "	" << avgRating << "		" << movieName << endl;
	}
	void Movie:: print2()
	{
		cout << " >> Movie Information <<" << endl << endl;

		cout << "Movie:  " << movieName << endl << "Year:   " << year << endl << "average rating   " << avgRating << endl << "numReviews   " << endl << quantity << endl;
		for (int i = 0; i < 5; i++)
		{
			cout << "Num-" << i + 1 << " " << ratings[i] << endl;
		}
	}