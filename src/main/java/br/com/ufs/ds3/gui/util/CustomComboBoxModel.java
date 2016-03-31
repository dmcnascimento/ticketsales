package br.com.ufs.ds3.gui.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.MutableComboBoxModel;
import javax.swing.event.ListDataListener;

public class CustomComboBoxModel<T> implements MutableComboBoxModel<T> {
	
	private List<Optional<T>> elements = new ArrayList<>();
	private Optional<T> selectedItem = Optional.empty();

	@SuppressWarnings("unchecked")
	@Override
	public void setSelectedItem(Object anItem) {
		selectedItem = (Optional<T>) Optional.ofNullable(anItem);
	}

	@Override
	public Object getSelectedItem() {
		return selectedItem.orElse(null);
	}

	@Override
	public int getSize() {
		return elements.size();
	}

	@Override
	public T getElementAt(int index) {
		return elements.get(index).orElse(null);
	}

	@Override
	public void addListDataListener(ListDataListener l) {
	}

	@Override
	public void removeListDataListener(ListDataListener l) {
	}

	@Override
	public void addElement(T item) {
		elements.add(Optional.ofNullable(item));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void removeElement(Object obj) {
		Optional<T> opt = (Optional<T>) Optional.ofNullable(obj);
		elements.remove(opt);
		if (opt.equals(selectedItem)) {
			selectedItem = Optional.empty();
		}
	}

	@Override
	public void insertElementAt(T item, int index) {
		elements.add(index, Optional.ofNullable(item));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void removeElementAt(int index) {
		Optional<T> opt = (Optional<T>) Optional.ofNullable(elements.get(index));
		elements.remove(index);
		if (opt.equals(selectedItem)) {
			selectedItem = Optional.empty();
		}
	}
}
