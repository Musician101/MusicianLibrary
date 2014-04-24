package musician101.luc.gui;

import java.util.Arrays;
import java.util.List;

import javax.swing.JComboBox;

@SuppressWarnings({ "serial", "rawtypes" })
public class GuiComboBox extends JComboBox
{
	@SuppressWarnings("unchecked")
	public GuiComboBox(Object[] items, int x, int y, int w, int h)
	{
		super(items);
		setBounds(x, y, w, h);
	}
	
	public void addAll(Object[] items)
	{
		addAll(Arrays.asList(items));
	}
	
	@SuppressWarnings("unchecked")
	public void addAll(List<Object> items)
	{
		for (Object item : items)
			addItem(item);
	}
}
