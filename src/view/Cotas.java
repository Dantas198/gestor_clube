package view;

import model.AlunoNaoExisteException;
import model.FacadeModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class Cotas extends JFrame implements Observer {
    private FacadeModel modelFacade;
    private JList list1;
    private JPanel panel3;
    private JButton pagarButton;
    private JButton confirmarButton;
    private JScrollPane scroll;
    private DefaultListModel<String> modelQ;
    private JFrame isto;

    private int id;

    public Cotas(int numero) {
        super("Pagamento");
        this.setSize(500, 500);
        this.add(panel3);
        this.setVisible(true);
        isto = this;
        id = numero;

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - this.getHeight()) / 2);
        this.setLocation(x, y);

        modelFacade = FacadeModel.getInstance();
        modelFacade.addObserver(this);

        // lista de cotas
        modelQ = new DefaultListModel<>();
        list1.setModel(modelQ);
        list1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list1.setLayoutOrientation(JList.VERTICAL);

        fillJListCotas();

        pagarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    modelFacade.pagarCota(id, 5.0);
                } catch (IOException i) {
                    JOptionPane.showMessageDialog(isto, "Não foi possível guardar.", "Erro", JOptionPane.ERROR_MESSAGE);
                } catch (AlunoNaoExisteException a) {
                    JOptionPane.showMessageDialog(isto, "O aluno não existe.","Erro", JOptionPane.ERROR_MESSAGE);
                    dispose();
                }
            }
        });
        confirmarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isto.dispose();
            }
        });
    }

    public void fillJListCotas (){
        this.modelQ.clear();
        try {
            //ArrayList<Double> valores = modelFacade.getCotasValor(id);
            ArrayList<LocalDate> datas = modelFacade.getCotasData(id);
                for (LocalDate a : datas) {
                    this.modelQ.addElement("Data: " + a + ", Valor: 5.0");
                }
        }catch(AlunoNaoExisteException a) {System.out.println("erro");}
    }

    public void update(Observable o, Object arg) {
        int atual = (int) arg;
        if(atual == 1) {
            fillJListCotas();
        }
    }
}
