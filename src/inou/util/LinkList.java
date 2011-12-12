/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.util;

import java.io.Serializable;
import java.util.Iterator;

public class LinkList implements Serializable {

    private Linkable head, terminal;

    private int count = 0;

    public LinkList() {
    }

    public void clear() {
        head = null;
        terminal = null;
        count = 0;
    }

    public void add(Linkable obj) {
        if (head == null) {
            head = obj;
            terminal = obj;
            obj.setNext(null);
        } else {
            terminal.setNext(obj);
            terminal = obj;
            terminal.setNext(null);
        }
        count++;
    }

    public Linkable get(int i) {
        int c = 0;
        Linkable pos = head;
        while (pos != null) {
            if (i == c)
                return pos;
            c++;
            pos = pos.next();
        }
        return null;
    }

    public Linkable head() {
        return head;
    }

    public Linkable tail() {
        return terminal;
    }

    public int size() {
        return count;
    }

    public boolean isEmpty() {
        return head == null;
    }

    public void remove(Linkable obj) {
        if (head == null) return;
        if (obj == head) {
            head = head.next();
            if (head == null) {
                terminal = null;
            }
        } else {
            Linkable prev = prev(obj);
            if (prev == null) {
                return;
            }
            prev.setNext(obj.next());
            if (obj.next() == null) {
                terminal = prev;
            }
        }
        count--;
    }

    public boolean contains(Linkable obj) {
        Linkable pos = head;
        while (pos != null) {
            if (pos == obj)
                return true;
            pos = pos.next();
        }
        return false;
    }

    private Linkable prev(Linkable obj) {
        if (head == obj)
            return null;
        Linkable prev = head;
        while (prev != null) {
            if (prev.next() == obj) {
                return prev;
            }
            prev = prev.next();
        }
        return null;
    }

    public static void main(String[] args) {
        LinkList list = new LinkList();
        Linker[] objs = {
                new Linker("1"),
                new Linker("2"),
                new Linker("3"),
                new Linker("4"),
                new Linker("5"),
                new Linker("6"),
        };
        list.add(objs[0]);
        list.add(objs[1]);
        list.add(objs[2]);
        list.add(objs[3]);
        list.add(objs[4]);
        list.add(objs[5]);
        System.out.println("ELM:"+toString(list));
        System.out.println("SIZE:"+list.size());
        list.remove(objs[2]);
        list.remove(objs[3]);
        System.out.println("ELM:"+toString(list));
        System.out.println("SIZE:"+list.size());
        list.remove(objs[0]);
        list.remove(objs[1]);
        list.remove(objs[4]);
        System.out.println("ELM:"+toString(list));
        System.out.println("SIZE:"+list.size());
        list.remove(objs[5]);
        System.out.println("ELM:"+toString(list));
        System.out.println("SIZE:"+list.size());
    }

    public static String toString(LinkList list) {
        String ret = "";
        Linkable pos = list.head();
        while (pos != null) {
            ret += pos.toString();
            pos = pos.next();
        }
        return ret;
    }

    static class Linker implements Linkable {
        String title;

        Linkable next;

        Linker(String title) {
            this.title = title;
        }

        public String toString() {
            return title;
        }

        public Linkable next() {
            return next;
        }

        public void setNext(Linkable link) {
            next = link;
        }
    }

    class LinkIterator implements Iterator {
        private Linkable node;

        public Object next() {
            Linkable ret = node;
            node = node.next();
            return ret;
        }

        public boolean hasNext() {
            if (node == null) {
                return false;
            }
            return true;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
