package pong.principal;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

import pong.audio.Audio;
import pong.grafico.Tela;

public class Principal implements Runnable {
	public static int pontosP1 = 0;
	public static int pontosP2 = 0;

	// janela
	private JFrame janela = new JFrame("Pong");
	//som
	Audio pong = new Audio("/Som/pong.wav");
	Audio pongPonto = new Audio("/Som/pongPonto.wav");

	// bola
	public static double xBola;
	public static double yBola;
	public static final int TAMANHO_BOLA = 10;
	private final int pixelsParaAndar = 1;
	private int angulo = 180;
	private Rectangle bolaRect;

	// player 1
	public static int xP1;
	public static int yP1;
	public static final int TAMANHO_PLAYER = 70;
	private int movimentoP1 = 0;
	// 0 = parado, 1 = para cima, 2 = para baixo
	private Rectangle p1Rect;
	
	// player 2
	public static int xP2;
	public static int yP2;
	private int movimentoP2 = 0;
	private Rectangle p2Rect;
	
	
	

	private boolean rodando = true;
	private boolean apertadoUp = false;
	private boolean apertadoDown = false;
	private boolean apertadoW = false;
	private boolean apertadoS = false;

	public Principal() {
		janela.setSize(800, 500);
		janela.setVisible(true);
		janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		janela.setLocationRelativeTo(null);
		janela.setResizable(false);

		xBola = janela.getWidth() / 2;
		yBola = janela.getHeight() / 2;

		xP1 = janela.getWidth() - 20;
		yP1 = janela.getHeight() / 2;
		
		xP2 = 5;
		yP2 = janela.getHeight() / 2;

		Tela tela = new Tela(janela, this);
		janela.setContentPane(tela);
		
		janela.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {

				if (e.getKeyCode() == KeyEvent.VK_UP && apertadoUp) {
					apertadoUp = false;
				}

				if (e.getKeyCode() == KeyEvent.VK_DOWN && apertadoDown) {
					apertadoDown = false;
				}

				if (!apertadoDown && !apertadoUp) {
					movimentoP1 = 0;
				}
				
				if (e.getKeyCode() == KeyEvent.VK_W && apertadoW) {
					apertadoW = false;
				}

				if (e.getKeyCode() == KeyEvent.VK_S && apertadoS) {
					apertadoS = false;
				}

				if (!apertadoS && !apertadoW) {
					movimentoP2 = 0;
				}

			}

			@Override
			public void keyPressed(KeyEvent e) {

				if (e.getKeyCode() == KeyEvent.VK_UP) {
					movimentoP1 = 1;
					apertadoUp = true;
				}

				if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					movimentoP1 = 2;
					apertadoDown = true;
				}
				
				if (e.getKeyCode() == KeyEvent.VK_W) {
					movimentoP2 = 1;
					apertadoW = true;
				}

				if (e.getKeyCode() == KeyEvent.VK_S) {
					movimentoP2 = 2;
					apertadoS = true;
				}
			}
		});

		new Thread(this).start();
	}
	
	private void novaBola(int quemPontuou) {
		pongPonto.play();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		xBola = janela.getWidth() / 2;
		yBola = janela.getHeight() / 2;
		if (quemPontuou == 1) {
			angulo = 180;
		} else {
			angulo = 0;
		}

	}

	private void moverBola() {
		double angulo = Math.toRadians(this.angulo);

		xBola += Math.cos(angulo) * pixelsParaAndar;
		yBola += Math.sin(angulo) * pixelsParaAndar;
		checaColisao();

	}
	
	private void checaColisao() {
		if (yBola < 27 || (yBola + TAMANHO_BOLA) > janela.getHeight() - 50) {
			colisao(true, 0, 0);
		}
		
		if (xBola < 0) {
			pontosP1++;
			novaBola(1);
		}
		
		if (xBola > janela.getWidth()) {
			pontosP2++;
			novaBola(2);
			
		}
	}
	
	private void checaColisaoPlayer() {
		bolaRect = new Rectangle((int)xBola - TAMANHO_BOLA / 2, (int)yBola - TAMANHO_BOLA / 2, TAMANHO_BOLA, TAMANHO_BOLA);
		p1Rect = new Rectangle(xP1 - 5, yP1 - TAMANHO_PLAYER / 2, 10, TAMANHO_PLAYER);
		p2Rect = new Rectangle(xP2 + 5, yP2 - TAMANHO_PLAYER / 2, 10, TAMANHO_PLAYER);
		
		if (bolaRect.intersects(p1Rect) && bolaRect.getX() <= janela.getWidth() - 29) {
			if (bolaRect.intersection(p1Rect).getY() <= p1Rect.getY() + 20) {
				
				int fator = (int)((bolaRect.getY() - p1Rect.getY()) - 20) * -1;
				
				colisao(false, fator, 1);
				xBola --;
				return;
			}
			
			if (bolaRect.intersection(p1Rect).getY() >= p1Rect.getY() + 50) {	
				
				int fator = (int)((bolaRect.getY() - p1Rect.getY()) - 50) *-1;
				colisao(false, fator, 1);
				xBola --;
				return;
			}
			
			colisao(false, 0, 1);
			xBola --;
		} else if(bolaRect.intersects(p1Rect)) {
			colisao(true, 0, 1);
		}
		
		if (bolaRect.intersects(p2Rect) && bolaRect.getX() <=  29) {
			if (bolaRect.intersection(p2Rect).getY() <= p2Rect.getY() + 20) {		
				int fator = (int)(bolaRect.getY() - p2Rect.getY()) - 20;
				colisao(false, fator, 2);
				xBola++;
				return;
			}
			
			if (bolaRect.intersection(p2Rect).getY() >= p2Rect.getY() + 50) {
				int fator = (int)(bolaRect.getY() - p2Rect.getY()) - 50;
				colisao(false, fator, 2);
				xBola++;
			}
			colisao(false, 0, 2);
		} else if(bolaRect.intersects(p2Rect)) {
			colisao(true, 0, 2);
		}
	}
	
	public void colisao(boolean vertical, int fator, int player) {
		pong.play();
		
		if (vertical) {
			angulo = (angulo - 360) * -1;
		} else {
			System.out.println(fator);
			angulo = (angulo - 180) * -1  + (fator * 2);	
			if (player == 1) {
				if (angulo >= 250) {
					angulo = 240;
				}
				
				if (angulo <= 120) {
					angulo = 130;
				}
			}
			
			if (player == 2) {
				if (angulo >= 70 && angulo <= 180) {
					angulo = 60;
				}
				
				if (angulo >= 180 && angulo <= 300) {
					angulo = 310;
				}
			}
			System.out.println("angulo " + angulo);
			
		}
		
	}
	
	

	private void moverPlayer() {
		

		if (movimentoP1 == 1) {
			if (yP1 > (TAMANHO_PLAYER / 2 + 25)) {
				yP1 -= 3;
			}
		} else if (movimentoP1 == 2) {
			if (yP1 < janela.getHeight() - (TAMANHO_PLAYER / 2 + 57)) {
				yP1 += 3;
			}
		}
		
		if (movimentoP2 == 1) {
			if (yP2 > (TAMANHO_PLAYER / 2 + 25)) {
				yP2 -= 3;
			}
		} else if (movimentoP2 == 2) {
			if (yP2 < janela.getHeight() - (TAMANHO_PLAYER / 2 + 57)) {
				yP2 += 3;
			}
		}
	}

	@Override
	public void run() {
		int randonAngulo = 1 + (int)(Math.random() * 2);
		if (randonAngulo == 1) {
			angulo = 180;
		} else {
			angulo = 0;
		}
		while (rodando) {
			moverPlayer();
			moverBola();
			checaColisaoPlayer();

			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	public static void main(String[] args) {
		@SuppressWarnings("unused")
		Principal main = new Principal();
	}

}
