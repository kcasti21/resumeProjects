// hw2.cpp : Defines the entry point for the console application.
//
#include "Movie.h"
#include "Reviewer.h"
#include "Review.h"
#include <iostream>
#include <iomanip>
#include <fstream>
#include <string>
#include <vector>
#include <sstream>
#include <algorithm>
#include <numeric>

using std::string;
using std::ifstream;
using std::vector;
using std::stringstream;
using std::getline;
using std::sort;
using std::cout;
using std::stoi;
using std::endl;

/*
class Reviewer
{
public:
	int user;
	int reviewCount;
	Reviewer(int UserID)
		:user(UserID), reviewCount(0)
	{}
		void reviewCountIncr()
	{
		reviewCount++;
	}
		void print()
		{
			cout << user << "	"<< reviewCount << endl;
		}
};
*/
/*
class Reviews
{
public:
	int movieID;
	int userID;
	int rating;
	string date;
	static int count;
	Review(int MovieID, int UserID, int Rating, string Date)
		:movieID(MovieID), userID(UserID), rating(Rating), date(Date)
	{}
	void setCount()
	{
		count = 0;
	}
	void incrCount()
	{
		count++;
	}
};
*/





vector<Movie> readMovies() //reads in data from movie.scv file
{
	vector<Movie> movies;
    ifstream file("movies.csv");
	if (!file.good())
	{
		return movies;
	}
	string movieName, line, ID, Year; //tmp variables used to read in data
	getline(file, line); //reject first line of the input
	while (getline(file, line)) //begin reading data
	{
		stringstream ss(line);
		getline(ss, ID, ',');
		getline(ss, movieName, ',');
		getline(ss, Year, ',');
		Movie m(stoi(ID), movieName, stoi(Year), 0.0, 0);
		movies.push_back(m);
	} //end reading data
	return movies;
}

vector<Review> readReviews() //reads in data from reviews.scv file
{
	vector<Review> reviews;
	ifstream file("reviews.csv");
	if (!file.good())
	{
		return reviews;
	}

	string movieID, line, userID, rating, Year; //tmp variables used to read in data
	getline(file, line); //reject first line of the input
	while (getline(file, line)) //begin reading data
	{
		stringstream ss(line);
		getline(ss, movieID, ',');
		getline(ss, userID, ',');
		getline(ss, rating, ',');
		getline(ss, Year, ',');
		Review r(stoi(movieID), stoi(userID), stoi(rating), Year);
		reviews.push_back(r);
	} //end reading data
	return reviews;
}





int main()
{
	vector<Movie> movies = readMovies();
	vector<Review> reviews = readReviews();

	//ssort by user id to create id classs
	sort(reviews.begin(),
		reviews.end(),
		[](const Review& r1, const Review& r2)
	{
		return r1.userID < r2.userID;
	}
	);
	vector<Reviewer> reviewers;
	int ID = reviews[0].userID;
	Reviewer rwer(reviews[0].userID, 0);
	reviewers.push_back(rwer);
	int index = 0;

	for (Review& r : reviews) //creates reviewer class
	{
		if (ID == r.userID && reviewers.size() == index + 1)
		{
			reviewers[index].reviewCountIncr();
		}
		else
		{
			Reviewer rwr(r.userID, 0);
			ID = r.userID;
			reviewers.push_back(rwr);
			index++;
			reviewers[index].reviewCountIncr();
		}
	} //end for that creates reviewer class


	sort(reviewers.begin(),
		reviewers.end(),
		[](const Reviewer& r1, const Reviewer& r2)
	{
		if (r1.reviewCount == r2.reviewCount)
			return r1.user < r2.user;

		return r1.reviewCount > r2.reviewCount;
	}

	);

	for (int i = 0; i < 10; i++)
	{
		cout << "reviewer: "  << reviewers[i].user << " = "<< reviewers[i].reviewCount;
    }

	
	sort( reviews.begin(), reviews.end(),
		[](const Review& r1, const Review& r2)
	{
		return r1.movieID < r2.movieID;
	}
	);
	sort(movies.begin(), movies.end(),
		[](Movie& m1, Movie& m2)
	{
		return m1.movieID < m2.movieID;
	});


	int ind = 0;
	for (Movie& m : movies)
	{
		ind = m.calcAverage(reviews, ind);
	}

	sort(movies.begin(), movies.end(), 
	[](Movie& m1, Movie& m2)
	{
		return m1.avgRating > m2.avgRating;
	});
	bool request = true; //used to drive whileloop
	cout << " ** netflix movies reviews " << endl << endl;
	cout << "reacing movies " << movies.size() << endl;
    cout << "reading reviews " << reviews.size() << endl<< endl;

	cout << ">> Top-10 movies <<  " << endl;
	for (int i = 0; i < 10; i++)
	{
		cout << i << ".	";
		movies[i].print();
	}

	cout << ">> Top-10 reviews <<" << endl;

	for (int i = 0; i < 10; i++)
	{
		cout << i << ".	";
		reviewers[i].print();
	}

	sort(movies.begin(), movies.end(),
		[](Movie& m1, Movie& m2)
	{
		return m1.movieID < m2.movieID;
	});

	int x;
	int max = movies.size();
	while (request == true)
	{
		cout << "enter a digit between 1 and " << max << " or 0 to terminate" << endl;
		std::cin >> x;
		if (x == 0)
		{
			cout << "** done **";
			request = false;
		}
		else if (x >= 1 && x <= max)
		{
			movies[x - 1].print2();
		}
		else
		{
			cout << "invalid entry";
		}
	}
	return 0;
}

