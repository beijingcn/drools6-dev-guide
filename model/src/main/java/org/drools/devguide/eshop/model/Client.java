package org.drools.devguide.eshop.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Client implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String id;
	private String name;
	private List<Order> orders = new ArrayList<>();
	
	public Client() {
	}

        public String getId() {
                return id;
        }

        public void setId(String id) {
               this.id = id;
        }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

        @Override
        public int hashCode() {
                int hash = 7;
                hash = 67 * hash + Objects.hashCode(this.id);
                hash = 67 * hash + Objects.hashCode(this.name);
                return hash;
        }

        @Override
        public boolean equals(Object obj) {
                if (obj == null) {
                    return false;
                }
                if (getClass() != obj.getClass()) {
                    return false;
                }
                final Client other = (Client) obj;
                if (!Objects.equals(this.id, other.id)) {
                    return false;
                }
                if (!Objects.equals(this.name, other.name)) {
                    return false;
                }
                return true;
        }

	

	@Override
	public String toString() {
		return "Client [name=" + name + ", orders=" + orders + "]";
	}
}
