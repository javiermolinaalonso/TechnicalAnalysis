package com.assets.bollinger.data.entities;

import java.util.List;

public class BollingerValues {

	private List<BollingerValue> values;
	
	public void setValues(List<BollingerValue> values) {
		this.values = values;
	}

	public List<BollingerValue> getValues(){
		return values;
	}
}
