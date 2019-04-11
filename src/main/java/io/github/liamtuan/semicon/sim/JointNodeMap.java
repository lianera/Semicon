package io.github.liamtuan.semicon.sim;

import io.github.liamtuan.semicon.core.Node;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import scala.collection.immutable.ListMap;

import java.util.*;

public class JointNodeMap {
    Map<Joint, Node> joint_node = null;
    Map<Node, Set<Joint>> node_joints = null;

    JointNodeMap(){
        joint_node = new HashMap<>();
        node_joints = new HashMap<>();
    }

    void put(Joint joint, Node node){
        joint_node.put(joint, node);
        Set<Joint> joints = node_joints.get(node);
        if(joints == null){
            joints = new HashSet<>();
            node_joints.put(node, joints);
        }
        joints.add(joint);
    }

    Node get(Joint joint){
        return joint_node.get(joint);
    }

    Joint[] getNodeJoints(Node node){
        Set<Joint> joints = node_joints.get(node);
        if(joints == null)
            return new Joint[0];
        return joints.toArray(new Joint[0]);
    }

    Joint[] getLinkedJoints(Joint joint){
        Node node = joint_node.get(joint);
        if(node == null)
            return new Joint[0];
        Set<Joint> joints = node_joints.get(node);
        Stack<BlockPos> open = new Stack<>();
        Set<BlockPos> closed  = new HashSet<>();

        open.push(joint.linkedJoint().pos);
        closed.add(joint.pos);

        List<Joint> results = new ArrayList<>();

        while(!open.isEmpty()){
            BlockPos pos = open.pop();
            closed.add(pos);

            // find cell joints
            for(EnumFacing facing : EnumFacing.values()){
                Joint celljoint = new Joint(pos, facing);
                if(joints.contains(celljoint)){
                    results.add(celljoint);
                    BlockPos target = celljoint.linkedJoint().pos;
                    if(!closed.contains(target))
                        open.push(target);
                }
            }
        }
        return results.toArray(new Joint[0]);
    }

    public Node[] getAllNodes(){
        return node_joints.keySet().toArray(new Node[0]);
    }

    void remove(Joint joint){
        Node node = joint_node.get(joint);
        Set<Joint> joints = node_joints.get(node);
        joints.remove(joint);
        if(joints.isEmpty())
            node_joints.remove(node);
        joint_node.remove(joint);
    }

    void replaceNode(Node oldnode, Node newnode){
        Set<Joint> joints = node_joints.get(oldnode);
        if(joints != null){
            for(Joint joint : joints){
                put(joint, newnode);
            }
            node_joints.remove(oldnode);
        }
    }
}
