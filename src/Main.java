public class Main {
	public volatile static int produtos = 0;
	public volatile static int produtoId = 0;
	public volatile static int produtoIdConsumido = - 1;
	
	public static volatile Object lock = new Object();
	
	public static void main(String[] args) {
		for(int i = 0; i <= 200; i++) {
			Produtor p1 = new Produtor(i);
			Consumidor c1 = new Consumidor(i);
			p1.start();
			c1.start();			
		}
	}
}

class Produtor extends Thread {
	int id;
	Produtor(int novoId ){
		this.id = novoId;
	}
	public void run() {
		synchronized( Main.lock ) 
		{
			System.out.println("Produtor " + this.id + " chegou!");
			if (Main.produtos >= 20) { // Buffer, estoque cheio!
				try {
					Main.lock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			Main.produtos = Main.produtos + 1;
			Main.produtoId = Main.produtoId + 1;
			System.out.println("Produtor " + this.id + " produziu item: " + Main.produtoId + " total de itens: " + Main.produtos);
			Main.lock.notify();
		} // fim do lock
	}
}

class Consumidor extends Thread {
	int id;
	Consumidor(int novoId){
		this.id = novoId;
	}
	public void run() {
		synchronized( Main.lock ) 
		{	
			System.out.println("Consumidor " + this.id + " encontrou: " + Main.produtos + " produto(s).");
			while ( Main.produtos <= 0) { // Estoque zerado !
				try {
					Main.lock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			Main.produtos = Main.produtos - 1;
			Main.produtoIdConsumido = Main.produtoIdConsumido + 1;
			System.out.println("Consumidor " + this.id + " consumiu item " + Main.produtoIdConsumido);
			Main.lock.notifyAll();
		} // fim do lock
	}
}