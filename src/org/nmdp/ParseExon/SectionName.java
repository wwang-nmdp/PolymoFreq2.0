package org.nmdp.ParseExon;

import java.util.Arrays;
import java.util.List;

public enum SectionName {
	US(0, 0),
	e1(1, 1),
	i1(0, 2),
	e2(1, 3),
	i2(0, 4),
	e3(1, 5),
	i3(0, 6),
	e4(1, 7),
	i4(0, 8),
	e5(1, 9),
	i5(0, 10),
	e6(1, 11),
	i6(0, 12),
	e7(1, 13),
	i7(0, 14),
	e8(1, 15),
	DS(0, 16);

	private int value;
	private int order;

	SectionName(int value, int order){
		this.value = value;
		this.order = order;
	}

	public boolean isExon(){
		return this.value == 1;
	}

	public SectionName get(int order){
		List<SectionName> values = Arrays.asList(SectionName.values());
		for(SectionName  item : values){
			if(item.order == order){
				return item;
			}
		}
		return null;
	}

	public int getOrder(SectionName sn){
		return sn.order;
	}
}
