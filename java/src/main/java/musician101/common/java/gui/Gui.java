package musician101.common.java.gui;

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

public class Gui
{
    public static class Button extends JButton
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

        public Button(String text, int x, int y, int w, int h, ActionListener listener)
        {
            super(text);
            setBounds(x, y, w, h);
            if (listener != null)
                addActionListener(listener);
        }
    }

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

        public void addAll(List<E> items)
        {
            items.forEach(this::addItem);
        }
    }

    public static class Label extends JLabel
    {
        public Label(String text, int x, int y)
        {
            super(text);
            setBounds(x, y, 0, 0);
            setSize(getPreferredSize());
        }
    }

    public static class Panel extends JPanel
    {
        public Panel(int w, int h)
        {
            setPreferredSize(new Dimension(w, h));
        }
    }

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

    public static class Table extends JTable
    {
        public Table(TableModel model)
        {
            super(model);
            setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        }
    }

    public static class TextField extends JTextField
    {
        public TextField(int x, int y, int w, int h)
        {
            setBounds(x, y, w, h);
        }
    }

    public static class MenuBar extends JMenuBar
    {
        public MenuBar(int w, int h, List<Menu> menus)
        {
            super();
            setBounds(0, 0, w, h);
            menus.forEach(this::add);
        }
    }

    public static class Menu extends JMenu
    {
        public Menu(String name, List<MenuItem> items)
        {
            super();
            setText(name);
            items.forEach(this::add);
        }
    }

    public static class MenuItem extends JMenuItem
    {
        public MenuItem(String name, ActionListener listener)
        {
            super();
            setText(name);
            addActionListener(listener);
        }
    }
}
