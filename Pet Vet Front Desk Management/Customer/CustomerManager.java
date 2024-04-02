package Customer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CustomerManager {

    private List<Person> customers;

    public CustomerManager() {
        this.customers = new ArrayList<>();
    }

    
    public void addCustomer(Person customer) {
        this.customers.add(customer);
    }

    // 删除客户信息
    public boolean removeCustomer(String email) {
        Iterator<Person> iterator = this.customers.iterator();
        while (iterator.hasNext()) {
            Person currentCustomer = iterator.next();
            if (currentCustomer.getEmail().equals(email)) {
                iterator.remove();
                return true; // 成功找到并删除了客户
            }
        }
        return false; // 未找到指定的客户
    }

    // 更新客户信息
    public boolean updateCustomer(String email, Person updatedCustomer) {
        for (int i = 0; i < this.customers.size(); i++) {
            if (this.customers.get(i).getEmail().equals(email)) {
                this.customers.set(i, updatedCustomer); // 替换为更新后的客户信息
                return true; // 成功更新了客户信息
            }
        }
        return false; // 未找到指定的客户进行更新
    }
    
    // 获取所有客户列表（如果需要的话）
    public List<Person> getAllCustomers() {
        return this.customers;
    }

    // 其他可能的方法：根据特定条件查找客户、获取单个客户的详细信息等。
}
