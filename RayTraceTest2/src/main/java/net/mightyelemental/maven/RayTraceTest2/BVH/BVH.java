package net.mightyelemental.maven.RayTraceTest2.BVH;

import java.util.ArrayList;
import java.util.List;

import net.mightyelemental.maven.RayTraceTest2.Ray;
import net.mightyelemental.maven.RayTraceTest2.objects.Renderable;
import net.mightyelemental.maven.RayTraceTest2.objects.Scene;

public class BVH {

	public static BVHNode topNode;

	/**
	 * Tests if the ray intersects with the BVH
	 * 
	 * @return a list of renderable objects to be tested further
	 */
	public static List<Renderable> intersects(final Ray r) {
		List<Renderable> rends = new ArrayList<Renderable>();
		List<BVHNode> nodesToVisit = new ArrayList<BVHNode>();
		nodesToVisit.add( topNode );
		while (!nodesToVisit.isEmpty()) {
			BVHNode curr = nodesToVisit.remove( 0 );
			if (curr.intersects( r )) {
				if (curr.left != null) nodesToVisit.add( curr.left );
				if (curr.right != null) nodesToVisit.add( curr.right );
				if (curr.left == null && curr.right == null) {// reached bottom node
					rends.addAll( curr.linkedObjs );
				}
			}
		}
		return rends;
	}

	public static void generateBVHTree(Scene worldScene) {
		generateBVHTree( worldScene.objectList );
	}

	public static void generateBVHTree(List<Renderable> rends) {
		BVHNode[] nodes = new BVHNode[rends.size()];
		for (int i = 0; i < rends.size(); i++) { nodes[i] = new BVHNode( rends.get( i ) ); }
		topNode = mergeNodesToTop( nodes )[0];
	}

	public static BVHNode[] mergeNodesToTop(BVHNode[] nodes) {// TODO: modify this to actually work
		if (nodes.length == 1) return nodes;
		int size = (int) Math.ceil( nodes.length / 2.0 );
		int isOdd = nodes.length % 2;// If it is odd, you don't want it merging something with nothing
		BVHNode[] nextLayer = new BVHNode[size];
		for (int i = 0; i < size - isOdd; i++) {
			nextLayer[i] = BVHNode.merge( nodes[2 * i], nodes[2 * i + 1] );
		}
		if (isOdd == 1) { nextLayer[size - 1] = nodes[nodes.length - 1]; } // carry odd node to next layer
		return mergeNodesToTop( nextLayer );
	}

}
