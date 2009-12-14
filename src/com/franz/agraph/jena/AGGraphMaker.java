package com.franz.agraph.jena;

import com.franz.agraph.repository.AGRepositoryConnection;
import com.hp.hpl.jena.graph.GraphMaker;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.shared.ReificationStyle;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

public class AGGraphMaker implements GraphMaker {

	private AGRepositoryConnection conn;
	private AGGraph defaultGraph;
	
	public AGGraphMaker(AGRepositoryConnection conn) {
		this.conn = conn;
	}

	public AGRepositoryConnection getRepositoryConnection() {
		return conn;
	}

	@Override
	public void close() {
	}

	@Override
	public AGGraph getGraph() {
		if (defaultGraph==null) {
			defaultGraph = new AGGraph(this, null);
		}
		return defaultGraph;
	}

	@Override
	public AGGraph createGraph() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AGGraph createGraph(String uri) {
		return createGraph(uri, false);
	}

	@Override
	public AGGraph createGraph(String uri, boolean strict) {
		// TODO: strictness
		return new AGGraph(this, Node.createURI(uri));
	}

	@Override
	public ReificationStyle getReificationStyle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasGraph(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ExtendedIterator<String> listGraphs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AGGraph openGraph() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AGGraph openGraph(String name) {
		return openGraph(name, false);
	}

	@Override
	public AGGraph openGraph(String uri, boolean strict) {
		// TODO deal with strictness
		return new AGGraph(this, Node.createURI(uri));
	}

	@Override
	public void removeGraph(String name) {
		// TODO Auto-generated method stub
		
	}

}