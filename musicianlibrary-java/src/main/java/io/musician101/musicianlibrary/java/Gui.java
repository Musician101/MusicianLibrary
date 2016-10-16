package io.musician101.musicianlibrary.java;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableModel;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings({"unused", "WeakerAccess"})
public class Gui
{
    private Gui()
    {

    }

    @SuppressWarnings("unused")
    public static class Button extends JButton//NOSONAR
    {
        public Button(int x, int y, int w, int h, ActionListener listener)
        {
            super();
            setBounds(x, y, w, h);
            addActionListener(listener);
        }

        public Button(String text, int x, int y, int w, int h)
        {
            this(text, x, y, w, h, null);
        }

        public Button(String text, int x, int y, int w, int h, ActionListener listener)//NOSONAR
        {
            super(text);
            setBounds(x, y, w, h);
            if (listener != null)
                addActionListener(listener);
        }
    }

    @SuppressWarnings("unused")
    public static class ComboBox<E> extends JComboBox<E>
    {
        public ComboBox(E[] items, int x, int y, int w, int h)
        {
            super(items);
            setBounds(x, y, w, h);
        }

        public void addAll(E[] items)
        {
            addAll(Arrays.asList(items));
        }

        public void addAll(List<E> items)//NOSONAR
        {
            items.forEach(this::addItem);
        }
    }

    @SuppressWarnings("unused")
    public static class Label extends JLabel
    {
        public Label(String text, int x, int y)
        {
            super(text);
            setBounds(x, y, 0, 0);
            setSize(getPreferredSize());
        }
    }

    @SuppressWarnings("unused")
    public static class Panel extends JPanel
    {
        public Panel(int w, int h)
        {
            setPreferredSize(new Dimension(w, h));
        }
    }

    @SuppressWarnings("unused")
    public static class ScrollPane extends JScrollPane
    {
        public ScrollPane(Table table, int x, int y, int w, int h)
        {
            super(table);
            setBounds(x, y, w, h);
        }

        public ScrollPane(JList list, int x, int y, int w, int h)
        {
            super(list);
            setBounds(x, y, w, h);
        }
    }

    @SuppressWarnings("unused")
    public static class Table extends JTable//NOSONAR
    {
        public Table(TableModel model)
        {
            super(model);
            setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        }
    }

    @SuppressWarnings("unused")
    public static class TextField extends JTextField//NOSONAR
    {
        public TextField(int x, int y, int w, int h)
        {
            setBounds(x, y, w, h);
        }
    }

    @SuppressWarnings("unused")
    public static class MenuBar extends JMenuBar
    {
        public MenuBar(int w, int h, List<Menu> menus)
        {
            super();
            setBounds(0, 0, w, h);
            menus.forEach(this::add);
        }
    }

    @SuppressWarnings("unused")
    public static class Menu extends JMenu//NOSONAR
    {
        public Menu(String name, List<MenuItem> items)
        {
            super();
            setText(name);
            items.forEach(this::add);
        }
    }

    @SuppressWarnings("unused")
    public static class MenuItem extends JMenuItem//NOSONAR
    {
        public MenuItem(String name, ActionListener listener)
        {
            super();
            setText(name);
            addActionListener(listener);
        }
    }
}
