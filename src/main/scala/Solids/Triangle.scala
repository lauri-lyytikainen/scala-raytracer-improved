package Solids

import RayMath.{Vector3D, Ray}
import Rendering.Material

class Triangle(val v1: Vector3D, val v2: Vector3D, val v3: Vector3D, material: Material) extends Solid(v1, material):

  private val normal: Vector3D =
    val edge1 = v2 - v1
    val edge2 = v3 - v1
    edge1.cross(edge2).normalize

  override def normalAt(point: Vector3D): Vector3D =
    normal

  override def calculateIntersection(ray: Ray): Option[Vector3D] =
    // calculate the intersection point
    // the triangle is defined by 3 points in clockwise order

    val edge1 = v2 - v1
    val edge2 = v3 - v1
    val h = ray.direction cross edge2
    val a = edge1 dot h
    if a > -0.0000001 && a < 0.0000001 then
      return None
    val f = 1.0 / a
    val s = ray.origin - v1
    val u = f * (s dot h)
    if u < 0.0 || u > 1.0 then
      return None
    val q = s cross edge1
    val v = f * (ray.direction dot q)
    if v < 0.0 || u + v > 1.0 then
      return None
    // at this stage we can compute t to find out where
    // the intersection point is on the line
    val t = f * (edge2 dot q)
    if t > 0.0000001 then
      // ray intersection
      val intersectionPoint = ray.origin + ray.direction * t
      Some(intersectionPoint)
    else
      // this means that there is a line intersection
      // but not a ray intersection
      None

  override def getTextureColor(point: Vector3D): Vector3D = this.material.color

  /**
   * Calculates the distance from the plane of the triangle to a point in space
   * @param pointInSpace the point in space
   * @return the distance
   */
  def distanceToPoint(pointInSpace: Vector3D): Double =
    val d = normal dot v1
    val dist = (normal dot pointInSpace) - d
    if dist < 0 then
      -dist
    else
      dist