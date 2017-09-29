/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Montessori;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nmohamed
 */
public class Nodetreegrid<String> {
 
    public String progress;
    public String id;
    public String name;
    public String noofplannedlessons;
    public String noofarchivedlessons;
    public String finalrating;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNoofplannedlessons() {
        return noofplannedlessons;
    }

    public void setNoofplannedlessons(String noofplannedlessons) {
        this.noofplannedlessons = noofplannedlessons;
    }

    public String getNoofarchivedlessons() {
        return noofarchivedlessons;
    }

    public void setNoofarchivedlessons(String noofarchivedlessons) {
        this.noofarchivedlessons = noofarchivedlessons;
    }

    public String getFinalrating() {
        return finalrating;
    }

    public void setFinalrating(String finalrating) {
        this.finalrating = finalrating;
    }

    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public List<Nodetreegrid<String>> children;
 
    /**
     * Default ctor.
     */
    public Nodetreegrid() {
        super();
    }
 
    /**
     * Convenience ctor to create a Node<T> with an instance of T.
     * @param data an instance of T.
     */
    public Nodetreegrid(String id,String name,String finalrating,String noofplannedlessons,String noofarchivedlessons,String progress) {
        this();
        
        setId(id);
        setName(name);
        setFinalrating(finalrating);
        setNoofplannedlessons(noofplannedlessons);
        setNoofarchivedlessons(noofarchivedlessons);
        setProgress(progress);

    }
     
    /**
     * Return the children of Node<T>. The Tree<T> is represented by a single
     * root Node<T> whose children are represented by a List<Node<T>>. Each of
     * these Node<T> elements in the List can have children. The getChildren()
     * method will return the children of a Node<T>.
     * @return the children of Node<T>
     */
    public List<Nodetreegrid<String>> getChildren() {
        if (this.children == null) {
            return new ArrayList<Nodetreegrid<String>>();
        }
        return this.children;
    }
 
    /**
     * Sets the children of a Node<T> object. See docs for getChildren() for
     * more information.
     * @param children the List<Node<T>> to set.
     */
    public void setChildren(List<Nodetreegrid<String>> children) {
        this.children = children;
    }
 
    /**
     * Returns the number of immediate children of this Node<T>.
     * @return the number of immediate children.
     */
    public int getNumberOfChildren() {
        if (children == null) {
            return 0;
        }
        return children.size();
    }
     
    /**
     * Adds a child to the list of children for this Node<T>. The addition of
     * the first child will create a new List<Node<T>>.
     * @param child a Node<T> object to set.
     */
    public void addChild(Nodetreegrid<String> child) {
        if (children == null) {
            children = new ArrayList<Nodetreegrid<String>>();
        }
        children.add(child);
    }
     
    /**
     * Inserts a Node<T> at the specified position in the child list. Will     * throw an ArrayIndexOutOfBoundsException if the index does not exist.
     * @param index the position to insert at.
     * @param child the Node<T> object to insert.
     * @throws IndexOutOfBoundsException if thrown.
     */
    public void insertChildAt(int index, Nodetreegrid<String> child) throws IndexOutOfBoundsException {
        if (index == getNumberOfChildren()) {
            // this is really an append
            addChild(child);
            return;
        } else {
            children.get(index); //just to throw the exception, and stop here
            children.add(index, child);
        }
    }
     
    /**
     * Remove the Node<T> element at index index of the List<Node<T>>.
     * @param index the index of the element to delete.
     * @throws IndexOutOfBoundsException if thrown.
     */
    public void removeChildAt(int index) throws IndexOutOfBoundsException {
        children.remove(index);
    }
 
    
     
//    public String toString() {
//        StringBuilder sb = new StringBuilder();
//        sb.append("{").append(getData().toString()).append(",[");
//        int i = 0;
//        for (Node<String> e : getChildren()) {
//            if (i > 0) {
//                sb.append(",");
//            }
//            sb.append(e.getData().toString());
//            i++;
//        }
//        sb.append("]").append("}");
//        return sb.toString();
//    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }
}
