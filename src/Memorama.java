import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

public class Memorama extends JFrame {

    private JPanel gamePanel;
    private JButton resetButton;
    private ArrayList<ImageIcon> images;
    private ArrayList<JButton> cards;
    private ArrayList<Integer> flippedIndices = new ArrayList<>();
    private int pairsFound = 0;

    // Constructor
    public Memorama() {
        setTitle("Memorama");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        gamePanel = new JPanel(new GridLayout(4, 6, 10, 10)); 
        add(gamePanel, BorderLayout.CENTER);

        resetButton = new JButton("Reiniciar");
        add(resetButton, BorderLayout.SOUTH);
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetGame();
            }
        });

        loadImages();
        createCards();
        setVisible(true);
    }

    // Cargar las imágenes desde el directorio 'src/images/'
    private void loadImages() {
        images = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            String path = "src/images/img" + i + ".png";
            ImageIcon image = scaleImage(new ImageIcon(path), 100, 150); 
            images.add(image);
            images.add(image);
        }
        Collections.shuffle(images);
    }

    // Crear las cartas en el tablero
    private void createCards() {
        gamePanel.removeAll();
        cards = new ArrayList<>();

        // Crear 12 cartas con la misma imagen en la parte posterior
        for (int i = 0; i < 24; i++) {
            JButton card = new JButton();
            card.setIcon(scaleImage(new ImageIcon("src/images/back.png"), 100, 150)); 
            card.setPreferredSize(new Dimension(100, 150));
            int index = i;

            // Acción al hacer clic en una carta
            card.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    flipCard(index);
                }
            });

            cards.add(card);
            gamePanel.add(card);
        }

        gamePanel.revalidate();
        gamePanel.repaint();
    }

    // Voltear la carta al hacer clic
    private void flipCard(int index) {
        if (flippedIndices.size() < 2 && !flippedIndices.contains(index)) {
            cards.get(index).setIcon(images.get(index));
            flippedIndices.add(index);

            // Verificar si se han volteado dos cartas
            if (flippedIndices.size() == 2) {
                Timer timer = new Timer(1000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        checkMatch();
                    }
                });
                timer.setRepeats(false);
                timer.start();
            }
        }
    }

    // Verificar si las dos cartas volteadas coinciden
    private void checkMatch() {
        int firstIndex = flippedIndices.get(0);
        int secondIndex = flippedIndices.get(1);

        if (images.get(firstIndex).equals(images.get(secondIndex))) {
            cards.get(firstIndex).setEnabled(false);
            cards.get(secondIndex).setEnabled(false);
            pairsFound++;
        } else {
            cards.get(firstIndex).setIcon(scaleImage(new ImageIcon("src/images/back.png"), 100, 150));
            cards.get(secondIndex).setIcon(scaleImage(new ImageIcon("src/images/back.png"), 100, 150));
        }

        flippedIndices.clear();

        // Si se encuentran todos los pares, mostrar mensaje de victoria
        if (pairsFound == 6) { 
            JOptionPane.showMessageDialog(this, "¡Ganaste!");
        }
    }

    // Reiniciar el juego
    private void resetGame() {
        flippedIndices.clear();
        pairsFound = 0;
        Collections.shuffle(images);
        createCards();
    }

    // Redimensionar las imágenes a un tamaño adecuado
    private ImageIcon scaleImage(ImageIcon icon, int width, int height) {
        Image img = icon.getImage();
        Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImg);
    }

    // Método principal para iniciar el juego
    public static void main(String[] args) {
        new Memorama();
    }
}
