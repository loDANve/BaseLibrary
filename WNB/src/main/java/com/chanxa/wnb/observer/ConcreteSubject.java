package com.chanxa.wnb.observer;

public interface ConcreteSubject {

	void addObserver(Observer observer);

	void removeObserver(Observer observer);

	void notifyDataChanged();
}
