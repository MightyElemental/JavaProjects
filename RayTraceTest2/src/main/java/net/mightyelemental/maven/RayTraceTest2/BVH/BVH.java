package net.mightyelemental.maven.RayTraceTest2.BVH;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import net.mightyelemental.maven.RayTraceTest2.Ray;
import net.mightyelemental.maven.RayTraceTest2.Utils;
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

	/** Constructs a binary tree from the top down with the given nodes */
	public static BVHNode recursiveConstructBVH(BVHNode[] nodes, int depth) {
		if (nodes.length == 1) return nodes[0];
		if (nodes.length <= 4) { return BVHNode.merge( nodes ); }
		Arrays.sort( nodes, Comparator.comparing( a -> getAxisCentroid( (BVHNode) a, depth ) ) );

		BVHNode left = recursiveConstructBVH( Utils.getTopHalf( nodes ), depth + 1 );
		BVHNode right = recursiveConstructBVH( Utils.getBottomHalf( nodes ), depth + 1 );

		return BVHNode.merge( left, right );
	}

	private static float getAxisCentroid(BVHNode node, int depth) {
		switch (depth % 3) {
		case 0:
			return node.box.getCenter().x;
		case 1:
			return node.box.getCenter().y;
		case 2:
			return node.box.getCenter().z;
		}
		return node.box.getCenter().x;
	}

	public static void generateBVHTree(Scene worldScene) {
		generateBVHTree( worldScene.objectList );
	}

	public static void generateBVHTree(List<Renderable> rends) {
//		int size = (int) Math.ceil( rends.size() / 3.0 );
//		BVHNode[] nodes = new BVHNode[size];
//		for (int i = 0; i < nodes.length; i++) {
//			nodes[i] = new BVHNode( rends.get( 3 * i ) );
//			try {
//				for (int j = 1; j < 3; j++) {
//					Renderable re = rends.get( 3 * i + j );
//					nodes[i].addObject( re );
//				}
//			} catch (IndexOutOfBoundsException e) {}
//		}
		BVHNode[] nodes = new BVHNode[rends.size()];
		for (int i = 0; i < nodes.length; i++) { nodes[i] = new BVHNode( rends.get( i ) ); }

		// Arrays.sort( nodes, Comparator.comparing( a -> ((BVHNode) a).box.getCenter().x ) );
		// topNode = mergeNodesToTop( nodes )[0];
		topNode = recursiveConstructBVH( nodes, 0 );
	}

	@Deprecated
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
