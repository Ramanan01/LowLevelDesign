package src.designpatterns.structural.proxy;

public class Main {
    public static void main(String[] args) {
        Image img1 = new ProxyImage("image1.png");
        Image img2 = new ProxyImage("image2.png");

        System.out.println("Images created. Not yet loaded.");

        img1.display(); // Loads and displays
        img1.display(); // Just displays

        img2.display(); // Loads and displays
    }
}

