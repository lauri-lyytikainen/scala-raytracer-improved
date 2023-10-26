package RayMath
import Solids.Solid


/**
 * Holds data about a ray intersection with a solid.
 * @param ray The ray that was used to find the intersection.
 * @param solid The solid that was intersected.
 * @param hitPos The position and data of the intersection.
 */

class RayHit(val ray: Ray, val solid: Solid, val hitPos: Vector3D):
  
  val normal: Vector3D = this.solid.normalAt(this.hitPos)
  
  override def toString: String = "RayHit: " + this.ray + " " + this.solid + " " + this.hitPos