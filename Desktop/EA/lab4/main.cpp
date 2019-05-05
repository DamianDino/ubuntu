#include <iostream>
#include <string>
using namespace std;

using Pixel = char;

class PixelMatrix {

	int width, height;
	Pixel pixels[100][100];

	public:
		int getWidth() const {return width;}
		int getHeight() const {return height;}
		Pixel getPixelAt(int x, int y) const {return pixels[x][y];}

		void setWidth (int w) {
			width = w;
		}
		void setHeight (int h) {
			height = h;
		}
		void setPixelAt(int x, int y, Pixel p) {pixels[x][y] = p;}
};

class Shape {
	int i;
	public:
		virtual void fillPixels(PixelMatrix& output) = 0;
		virtual string getName() const = 0;
};

class Rectangle : public Shape {
	int lungime, latime;
	
	public:
		Rectangle(int _lungime, int _latime) : lungime (_lungime), latime (_latime) {}
		virtual void fillPixels(PixelMatrix& output) override {
			output.setWidth(lungime);
			output.setHeight(latime);
			//prima linie + ultima linie
			for(int i=0 ;i <lungime; ++i) {
				output.setPixelAt(i, 0, 'x');
				output.setPixelAt(i, latime-1, 'x');
			}
			for(int j=1; j<latime-1; ++j) {
				output.setPixelAt(0, j, 'x');
				output.setPixelAt(lungime-1, j, 'x');
			}
		}
		virtual std::string getName() const override {
			//return "rectangle";
		}
};


class Screen {
	public : 
		//void draw(Shape s); //prin valoare
		void draw(const Shape& s){  //prin referinta
			//std::cout << s.getName() << std::endl;
			PixelMatrix pixels;
			s.fillPixels(pixels);
			for(int i=0; i<pixels.getHeight(); ++i){ //linii
				for(int j=0; j<pixels.getWidth(); ++j){ //coloane
					std::cout << pixels.getPixelAt(j,i); //linie
				}
				std::cout<<std::endl;
			}
		}
};


int main (){

	Rectangle r(20, 10);
	Screen s;
	s.draw(r);
	return 0;
}