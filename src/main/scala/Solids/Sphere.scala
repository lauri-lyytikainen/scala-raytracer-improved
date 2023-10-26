package Solids

import RayMath.{Ray, RayHit, Vector3D}
import Rendering.Material
import scalafx.scene.paint.Color

/**
 * A sphere solid
 * @param initialPosition The position of the sphere
 * @param radius The radius of the sphere
 * @param material The material of the sphere
 */

class Sphere(initialPosition: Vector3D, var radius: Double, material: Material) extends Solid(initialPosition, material):

  override def calculateIntersection(ray: Ray): Option[Vector3D] =
    val t = (this.position - ray.origin).dot(ray.direction)
    val p = ray.origin + (ray.direction *(t))

    val y = (this.position - p).length

    if y < this.radius then
      val x = Math.sqrt(this.radius * this.radius - y * y)
      val t1 = t - x
      val t2 = t + x
      if t1 > 0 then
        Some(ray.origin + (ray.direction * (t1)))
      else if t2 > 0 then
        Some(ray.origin + (ray.direction * (t2)))
      else
        None
    else
      None

  override def normalAt(point: Vector3D): Vector3D =
    (point - (position)).normalize

  override def getTextureColor(point: Vector3D): Vector3D = this.material.color