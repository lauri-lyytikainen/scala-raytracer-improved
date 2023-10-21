package Solids

import RayMath.{Vector3D, Ray}
import Rendering.Material

class Triangle(val v1: Vector3D, val v2: Vector3D, val v3: Vector3D, material: Material) extends Solid(v1, material):
  def normalAt(point: Vector3D): Vector3D =
    val v1v2 = v2 - v1
    val v1v3 = v3 - v1
    val normal = v1v2 cross v1v3
    normal.normalize

  def calculateIntersection(ray: Ray): Option[Vector3D] =
    val normal = normalAt(v1)
    val denom = normal dot ray.direction
    if (denom > 1e-6) then
      val v1v0 = v1 - ray.origin
      val t = (v1v0 dot normal) / denom
      if (t >= 0) then
        val p = ray.origin + ray.direction * t
        val edge0 = v2 - v1
        val vp0 = p - v1
        if ((normal dot (edge0 cross vp0)) >= 0) then
          val edge1 = v3 - v2
          val vp1 = p - v2
          if ((normal dot (edge1 cross vp1)) >= 0) then
            val edge2 = v1 - v3
            val vp2 = p - v3
            if ((normal dot (edge2 cross vp2)) >= 0) then
              Some(p)
            else None
          else None
        else None
      else None
    else None

  override def getTextureColor(point: Vector3D): Vector3D = this.material.color
