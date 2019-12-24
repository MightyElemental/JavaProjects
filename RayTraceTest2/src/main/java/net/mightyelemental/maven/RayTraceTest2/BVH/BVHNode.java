package net.mightyelemental.maven.RayTraceTest2.BVH;

import java.util.ArrayList;
import java.util.List;

import net.mightyelemental.maven.RayTraceTest2.Ray;
import net.mightyelemental.maven.RayTraceTest2.Utils;
import net.mightyelemental.maven.RayTraceTest2.objects.BoundingBox;
import net.mightyelemental.maven.RayTraceTest2.objects.Renderable;

public class BVHNode {

	public List<Renderable> linkedObjs = new ArrayList<Renderable>();

	public BoundingBox box;

	public BVHNode left, right;

	private BVHNode() {}

	public BVHNode(Renderable rend) {
		if (rend == null) return;
		linkedObjs.add( rend );
		box = rend.generateBoundingBox();
	}

	public BVHNode(Renderable... rends) {
		if (rends == null || rends.length == 0) return;
		box = rends[0].generateBoundingBox();
		for (Renderable rend : rends) {
			box = Utils.mergeBoxes( box, rend.generateBoundingBox() );
			linkedObjs.add( rend );
		}
	}

	public void addObject(Renderable rend) {
		if (rend == null) return;
		box = Utils.mergeBoxes( box, rend.generateBoundingBox() );
		linkedObjs.add( rend );
	}

	/**
	 * Allows for nicer calling to {@link BoundingBox#intersects(Ray)}
	 */
	public boolean intersects(Ray r) { return box.intersects( r ); }

	public static BVHNode merge(BVHNode[] nodes) {
		BVHNode node = new BVHNode();

		node.box = nodes[0].box;
		for (BVHNode n : nodes) {
			node.box = Utils.mergeBoxes( node.box, n.box );
			node.linkedObjs.addAll( n.linkedObjs );
		}
		return node;
	}

	public static BVHNode merge(BVHNode n1, BVHNode n2) {
		BVHNode node = new BVHNode();
		node.box = Utils.mergeBoxes( n1.box, n2.box );
		node.linkedObjs.addAll( n1.linkedObjs );
		node.linkedObjs.addAll( n2.linkedObjs );
		node.left = n1;
		node.right = n2;
		return node;
	}

}
