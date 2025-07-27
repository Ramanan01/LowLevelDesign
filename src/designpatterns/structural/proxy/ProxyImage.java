package src.designpatterns.structural.proxy;

public class ProxyImage implements Image{
    private RealImage realImage;
    private final String filename;

    public ProxyImage(String filename){
        this.filename = filename;
    }

    @Override
    public void display(){
        if (realImage == null) {
            System.out.println("Creating RealImage object...");
            realImage = new RealImage(filename);
        } else {
            System.out.println("Using already loaded RealImage.");
        }
        realImage.display();
    }
}
