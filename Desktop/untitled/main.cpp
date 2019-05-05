#include <iostream>

struct FormGeom{
    virtual float getArea() = 0;

    FormGeom (){
        std::cout<<"constructor forma geom\n";
    }

    virtual ~FormGeom {
        std::cout<<"destructor forma geom\n";
    };

};

struct Circle : public FormGeom {
    float radius;

    Circle (int r): radius (r) {
        std::cout<<"constructor circle\n";
    }

    float getArea()  {
        return 3.14f * radius * radius;
    }

    ~Circle() {
        std::cout<<"destructor circle\n";
    }
};


int main() {
    FormGeom* f = new Circle{5};
    std::cout<< f->getArea() << "\n";
    delete f;
    return 0;
}