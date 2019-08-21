package com.daxiong.fun.model;

import java.util.ArrayList;
import java.util.List;

public class ProvincesGson {
	List<Province> provinces = new ArrayList<Province>();

	public List<Province> getProvinces() {
		return provinces;
	}

	public void setProvinces(List<Province> provinces) {
		this.provinces = provinces;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((provinces == null) ? 0 : provinces.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProvincesGson other = (ProvincesGson) obj;
		if (provinces == null) {
			if (other.provinces != null)
				return false;
		} else if (!provinces.equals(other.provinces))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ProvincesGson [provinces=" + provinces + "]";
	}
}
