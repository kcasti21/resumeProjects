#pragma once
#include <string>
#include <vector>
#include "Review.h"
class Movie
{
public:
	std::string movieName;
    int movieID;
    int year;
    double avgRating;
	int quantity;
	int ratings[5];
	Movie(int ID, std::string name, int year, double rating, int quant);
	void setArray();
	void update(int i);
	int calcAverage(const std::vector<Review>& reviews, int index);
	void print();
	void print2();
};