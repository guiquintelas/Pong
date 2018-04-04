package pong.grafico;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

import pong.principal.Principal;

@SuppressWarnings("serial")
public class Tela extends JPanel implements Runnable {
	
	private JFrame janela;
	private BufferedImage imagemParaPintar;
	private Graphics2D g2;
	private boolean rodando = true;
	
	public Tela(JFrame janela, Principal principal) {
		this.janela = janela;
		imagemParaPintar = new BufferedImage(janela.getWidth(), janela.getHeight(), BufferedImage.TYPE_INT_ARGB);
		g2 = (Graphics2D) imagemParaPintar.getGraphics();
		this.setBackground(Color.BLACK);
		
		new Thread(this).start();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(imagemParaPintar, 0, 0, this);
		
	}
	
	private void pintarPontos() {
		g2.setColor(Color.WHITE);
		g2.setFont(new Font("Stencil", Font.PLAIN, 40));
		g2.drawString(Integer.toString(Principal.pontosP1), janela.getWidth() / 2 + 10, 55 );
		if (Principal.pontosP2 >= 10) {
			g2.drawString(Integer.toString(Principal.pontosP2), janela.getWidth() / 2 - 60, 55 );
		} else {
			g2.drawString(Integer.toString(Principal.pontosP2), janela.getWidth() / 2 - 35, 55 );
		}
		
		
	}
	
	private void pintarPlayers() {
		g2.setColor(Color.WHITE);
		g2.fillRect(Principal.xP1 - 5, Principal.yP1 - Principal.TAMANHO_PLAYER / 2, 10, Principal.TAMANHO_PLAYER);
		g2.fillRect(Principal.xP2 + 5, Principal.yP2 - Principal.TAMANHO_PLAYER / 2, 10, Principal.TAMANHO_PLAYER);
		
	}
	
	private void pintarBola() {
		g2.setColor(Color.WHITE);
		g2.fillRect((int)Principal.xBola - Principal.TAMANHO_BOLA / 2,
				(int)Principal.yBola - Principal.TAMANHO_BOLA / 2, Principal.TAMANHO_BOLA, Principal.TAMANHO_BOLA);
	}
	
	private void resetarImagem() {
		imagemParaPintar = new BufferedImage(janela.getWidth(), janela.getHeight(), BufferedImage.TYPE_INT_ARGB);
		g2 = (Graphics2D) imagemParaPintar.getGraphics();
		g2.fillRect(5, 5, imagemParaPintar.getWidth() - 15, 15);	
		g2.fillRect(5, imagemParaPintar.getHeight() - 45, imagemParaPintar.getWidth() - 15, 15);
		g2.drawLine(imagemParaPintar.getWidth() / 2, 5, imagemParaPintar.getWidth() / 2, imagemParaPintar.getHeight() - 50);
		
		g2.setFont(new Font("Arial", Font.PLAIN, 30));
		g2.drawString("P1", janela.getWidth() - 80, 50);
		g2.drawString("P2", 80, 50);
		g2.setFont(new Font("Arial", Font.PLAIN, 15));
		g2.drawString("\u2191", janela.getWidth() - 95, 35);
		g2.drawString("\u2193", janela.getWidth() - 95, 50);
		g2.setFont(new Font("Arial", Font.PLAIN, 12));
		g2.drawString("W", 120, 37);
		g2.drawString("S", 121, 50);
	}

	
	@Override
	public void run() {
		while(rodando) {		
			resetarImagem();
			pintarPontos();
			pintarPlayers();
			pintarBola();
			
			repaint();
			
			try {
				Thread.sleep(1000/60);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}

	

}
