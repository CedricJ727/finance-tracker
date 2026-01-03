package model;

import java.util.*;

/**
 * Collection of expenses identified by a unique ID.
 */
public class Ledger implements Collection<Expense> {
    private HashMap<UUID, Expense> expenses;

    /**
     * Constructs an empty ledger.
     */
    public Ledger() {
        this.expenses = new HashMap<>();
    }

    /**
     * Adds expense to the ledger, indexed by its unique ID.
     *
     * @param expense the expense to add
     */
    public void addExpense(Expense expense) {
        this.expenses.put(expense.getId(), expense);
    }

    /**
     * Removes an expense with the ID from the ledger.
     *
     * @param id the unique ID of the expense that will be removed
     */
    public void removeExpense(UUID id) {
        this.expenses.remove(id);
    }

    /**
     * Gets the expense associated with the given ID.
     *
     * @param id the ID of the expense
     * @return the expense
     */
    public Expense getExpense(UUID id) {
        return this.expenses.get(id);
    }

    /**
     * Returns a collection view of all expenses in the ledger.
     *
     * @return a collection containing all expenses
     */
    public Collection<Expense> getAllExpenses() {
        return this.expenses.values();
    }

    @Override
    public int size() {
        return this.expenses.size();
    }

    @Override
    public boolean isEmpty() {
        return this.expenses.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        if (!(o instanceof Expense expense)) {
            return false;
        }
        return this.expenses.containsValue(expense);
    }

    @Override
    public Iterator<Expense> iterator() {
        return this.expenses.values().iterator();
    }

    @Override
    public Object[] toArray() {
        return this.expenses.values().toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return this.expenses.values().toArray(a);
    }

    @Override
    public boolean add(Expense expense) {
        if (expense == null) {
            return false;
        }
        this.expenses.put(expense.getId(), expense);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (o instanceof Expense expense) {
            return this.expenses.remove(expense.getId()) != null;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return this.expenses.values().containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends Expense> c) {
        boolean modified = false;
        for (Expense expense : c) {
            if (this.add(expense)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object o : c) {
            if (this.remove(o)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return this.expenses.values().retainAll(c);
    }

    @Override
    public void clear() {
        this.expenses.clear();
    }

}
