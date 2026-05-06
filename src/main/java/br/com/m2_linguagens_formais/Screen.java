package br.com.m2_linguagens_formais;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;

public class Screen extends JFrame {

    private static class RendererCategoria extends JPanel implements TableCellRenderer {
        private final JLabel icone  = new JLabel();
        private final JLabel rotulo = new JLabel();

        RendererCategoria() {
            setLayout(new FlowLayout(FlowLayout.LEFT, 6, 0));
            setOpaque(true);
            icone.setFont(new Font("SansSerif", Font.PLAIN, 11));
            rotulo.setFont(new Font("JetBrains Mono", Font.PLAIN, 12));
            add(icone);
            add(rotulo);
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {

            if (value instanceof Automato.Resultado resultado) {
                Color cor = switch (resultado.tipo()) {
                    case VALIDA                      -> new Color(0x22C55E);
                    case OPERADOR                    -> new Color(0xF59E0B);
                    case ERRO_INVALIDA, ERRO_SIMBOLOS -> new Color(0xEF4444);
                };
                icone.setText("●");
                icone.setForeground(cor);
                rotulo.setText(resultado.categoria());
                rotulo.setForeground(new Color(0x1A1A2E));
            }
            setBackground(row % 2 == 0 ? new Color(0xFFFFFF) : new Color(0xF8F9FC));
            return this;
        }
    }

    private static class RendererValor extends JLabel implements TableCellRenderer {
        RendererValor() {
            setFont(new Font("JetBrains Mono", Font.BOLD, 12));
            setForeground(new Color(0x1A1A2E));
            setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 8));
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {

            if (value instanceof Automato.Resultado resultado) {
                String valor = resultado.valor();
                FontMetrics fm    = getFontMetrics(getFont());
                int larguraDisp   = table.getColumnModel().getColumn(column).getWidth() - 12;  // desconta padding
                String exibir     = valor;
                if (fm.stringWidth(valor) > larguraDisp) {
                    while (fm.stringWidth(exibir + "...") > larguraDisp && !exibir.isEmpty()) {
                        exibir = exibir.substring(0, exibir.length() - 1);
                    }
                    exibir += "...";
                }
                setText(exibir);
                setToolTipText(null);   // tooltip gerenciado pela tabela
            }
            setBackground(row % 2 == 0 ? new Color(0xFFFFFF) : new Color(0xF8F9FC));
            return this;
        }
    }

    private JTextArea         entradaTopo;
    private DefaultTableModel modeloTabela;

    private final Automato automato = new Automato();

    private static final Color COR_FUNDO_PRINCIPAL  = new Color(0xF5F5F5);
    private static final Color COR_FUNDO_AREA       = new Color(0xFFFFFF);
    private static final Color COR_TEXTO            = new Color(0x1A1A2E);
    private static final Color COR_BOTAO_PRIMARIO   = new Color(0x4A6CF7);
    private static final Color COR_BOTAO_SECUNDARIO = new Color(0xDEDFEA);
    private static final Color COR_BORDA            = new Color(0xCBD5E1);

    public Screen() {
        configurarJanela();
        inicializarComponentes();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void configurarJanela() {
        setTitle("Reconhecedor de Linguagem Regular");
        setSize(680, 560);
        setMinimumSize(new Dimension(500, 400));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(COR_FUNDO_PRINCIPAL);
    }

    private void inicializarComponentes() {
        add(criarPainelBotoes(), BorderLayout.NORTH);
        add(criarSplitPane(),    BorderLayout.CENTER);
    }


    private JScrollPane criarPainelEntrada() {
        entradaTopo = new JTextArea();
        entradaTopo.setLineWrap(true);
        entradaTopo.setBackground(COR_FUNDO_AREA);
        entradaTopo.setForeground(COR_TEXTO);
        entradaTopo.setCaretColor(COR_TEXTO);
        entradaTopo.setFont(new Font("JetBrains Mono", Font.PLAIN, 12));
        entradaTopo.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

        JScrollPane rolagem = new JScrollPane(entradaTopo);

        rolagem.getVerticalScrollBar().setUI(new ScrollBar());
        rolagem.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));

        rolagem.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(COR_BORDA, 1),
                "Entrada",
                0, 0,
                new Font("SansSerif", Font.BOLD, 13),
                COR_TEXTO
        ));
        rolagem.getViewport().setBackground(COR_FUNDO_AREA);
        return rolagem;
    }


    private JSplitPane criarSplitPane() {
        JSplitPane split = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                criarPainelEntrada(),
                criarAreaSaida()
        );
        split.setResizeWeight(0.4);
        split.setDividerSize(6);
        split.setBorder(BorderFactory.createEmptyBorder(0, 8, 8, 8));
        split.setBackground(COR_FUNDO_PRINCIPAL);
        return split;
    }

    private JPanel criarPainelBotoes() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        painel.setBackground(COR_FUNDO_PRINCIPAL);
        painel.add(criarBotaoAnalisar());
        painel.add(criarBotaoLimpar());
        return painel;
    }

    private JButton criarBotaoAnalisar() {
        JButton botao = new JButton("Analisar");
        botao.setBackground(COR_BOTAO_PRIMARIO);
        botao.setForeground(Color.WHITE);
        botao.setFont(new Font("SansSerif", Font.BOLD, 13));
        botao.setFocusPainted(false);
        botao.setBorderPainted(false);
        botao.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        botao.addActionListener(e -> aoAnalisar());
        return botao;
    }

    private JButton criarBotaoLimpar() {
        JButton botao = new JButton("Limpar");
        botao.setBackground(COR_BOTAO_SECUNDARIO);
        botao.setForeground(COR_TEXTO);
        botao.setFont(new Font("SansSerif", Font.BOLD, 13));
        botao.setFocusPainted(false);
        botao.setBorderPainted(false);
        botao.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        botao.addActionListener(e -> aoLimpar());
        return botao;
    }

    private JScrollPane criarAreaSaida() {
        modeloTabela = new DefaultTableModel(new Object[]{"Categoria", "Valor"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable tabela = new JTable(modeloTabela) {

            @Override
            public String getToolTipText(java.awt.event.MouseEvent e) {
                int row = rowAtPoint(e.getPoint());
                int col = columnAtPoint(e.getPoint());
                if (row >= 0 && col >= 0) {
                    Object value = getValueAt(row, col);
                    if (value instanceof Automato.Resultado resultado) {
                        return resultado.valor(); // exibe a sentença completa no tooltip
                    }
                }
                return null;
            }

            @Override
            public JToolTip createToolTip() {
                JToolTip tip = new JToolTip();
                tip.setBackground(COR_FUNDO_AREA); // branco
                tip.setForeground(COR_TEXTO); // azul-escuro #1A1A2E
                tip.setFont(new Font("JetBrains Mono", Font.PLAIN, 12));
                tip.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(COR_BOTAO_PRIMARIO, 1), // borda azul
                        BorderFactory.createEmptyBorder(6, 12, 6, 12) // espaçamento interno
                ));
                tip.setOpaque(true);
                tip.setComponent(this);
                return tip;
            }
        };

        tabela.setShowGrid(false);
        tabela.setIntercellSpacing(new Dimension(0, 0));
        tabela.setRowHeight(26);
        tabela.setBackground(COR_FUNDO_AREA);
        tabela.setFocusable(false);
        tabela.setRowSelectionAllowed(false);
        tabela.setTableHeader(null);

        tabela.getColumnModel().getColumn(0).setPreferredWidth(290);
        tabela.getColumnModel().getColumn(0).setMinWidth(290);
        tabela.getColumnModel().getColumn(0).setMaxWidth(290);

        tabela.getColumnModel().getColumn(0).setCellRenderer(new RendererCategoria());
        tabela.getColumnModel().getColumn(1).setCellRenderer(new RendererValor());

        JScrollPane rolagem = new JScrollPane(tabela);

        rolagem.getVerticalScrollBar().setUI(new ScrollBar());
        rolagem.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));

        rolagem.getViewport().setBackground(COR_FUNDO_AREA);
        rolagem.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(COR_BORDA, 1),
                "Resultado",
                0, 0,
                new Font("SansSerif", Font.BOLD, 13),
                COR_TEXTO
        ));
        return rolagem;
    }

    private void aoAnalisar() {
        String texto = entradaTopo.getText();

        if (texto.trim().isEmpty()) {
            modeloTabela.setRowCount(0);
            Automato.Resultado aviso = new Automato.Resultado(
                    "campo não pode ser vazio", "", Automato.Resultado.Tipo.ERRO_INVALIDA);
            modeloTabela.addRow(new Object[]{ aviso, aviso });
            return;
        }

        modeloTabela.setRowCount(0);

        List<Automato.Resultado> resultados = automato.processarEntrada(texto);
        for (Automato.Resultado resultado : resultados) {
            modeloTabela.addRow(new Object[]{ resultado, resultado });
        }
    }

    private void aoLimpar() {
        entradaTopo.setText("");
        modeloTabela.setRowCount(0);
    }

    private static class ScrollBar extends javax.swing.plaf.basic.BasicScrollBarUI {

        @Override protected JButton createDecreaseButton(int o) { return new JButton() {{ setPreferredSize(new Dimension(0, 0)); }}; }
        @Override protected JButton createIncreaseButton(int o) { return new JButton() {{ setPreferredSize(new Dimension(0, 0)); }}; }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(0xA0ABBE));
            g2.fillRoundRect(r.x + 2, r.y + 2, r.width - 4, r.height - 4, 8, 8);
            g2.dispose();
        }
    }
}