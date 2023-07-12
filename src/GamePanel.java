import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    
    static final int WIDTH = 600;
    static final int HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (WIDTH * HEIGHT)/UNIT_SIZE;
    static final int DELAY = 75;
    
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];

    int bodyParts = 5;
    int foodEaten;
    int foodX;
    int foodY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;

    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.darkGray);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        newFood();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
    
    public void draw(Graphics g) {
        if(running){
            g.setColor(Color.RED);
            g.fillOval(foodX, foodY, UNIT_SIZE, UNIT_SIZE);

            for(int i=0; i<bodyParts; i++){
                if(i == 0){
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
                else{
                    g.setColor(Color.orange);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            g.setColor(Color.red);
			g.setFont( new Font("Ink Free",Font.BOLD, 40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: "+foodEaten, (WIDTH - metrics.stringWidth("Score: "+foodEaten))/2, g.getFont().getSize());
        }
        else{
            gameOver(g);
        }    
    }

    public void move() {
        for(int i=bodyParts;i>0;i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch (direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
    
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
    
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;

            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }

    }

    public void newFood(){
        foodX = random.nextInt((int)(WIDTH/UNIT_SIZE))*UNIT_SIZE;
        foodY = random.nextInt((int)(HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }

    public void checkFood(){
        if((x[0] == foodX) && (y[0] == foodY)){
            bodyParts++;
            foodEaten++;
            newFood();
        }
    }

    public void checkCollisions(){
        for(int i=bodyParts; i>0; i--){
            if((x[0] == x[i]) && (y[0] == y[i])){
                running = false;
            }
        }
        // Left Border
        if(x[0] < 0){
            running = false;
        }

        // Right BOrder
        if(x[0] > WIDTH){
            running = false;
        }

        // Top Border
        if(y[0] < 0){
            running = false;
        }

        // Bottom Border
        if(y[0] > HEIGHT){
            running = false;
        }

        if(!running){
            timer.stop();
        }

    }
    
    public void gameOver(Graphics g){
        g.setColor(Color.RED);
        g.setFont( new Font("Ink Free",Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("GAME OVER", (WIDTH - metrics.stringWidth("Game Over"))/2, HEIGHT/2);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(running){
            move();
            checkFood();
            checkCollisions();
        }

        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A:
                        if(direction != 'R'){
                            direction = 'L';
                        }
                    break;
            
                case KeyEvent.VK_D:
                        if(direction != 'L'){
                            direction = 'R';
                        }
                    break;

                case KeyEvent.VK_W:
                        if(direction != 'D'){
                            direction = 'U';
                        }
                    break;

                case KeyEvent.VK_S:
                        if(direction != 'U'){
                            direction = 'D';
                        }
                    break;
            }
        }
    }
}