package Rendering

import RayMath.{Ray, RayHit, Vector3D}
import Solids.Solid

import scala.collection.mutable

//import scalafx.scene.paint.Color

import scala.collection.mutable.Buffer

/**
 * Represents a scene with a camera, a list of solids, and a list of lights.
 */

class World():
  val camera = new Camera(new Vector3D(0, 0, 0), Math.toRadians(50))
  
  private val solids = mutable.Buffer[Solid]()

  def addSolid(solid: Solid): Unit =
    solids += solid

  def traceRay(ray: Ray): Option[RayHit] =

    var closestHit: Option[RayHit] = None
    for solid <- solids do
      var hit: Option[Vector3D] = None
      hit = solid.calculateIntersection(ray)
      if hit.isDefined && (closestHit.isEmpty || hit.get.distance(ray.origin) < closestHit.get.hitPos.distance(ray.origin)) then
        closestHit = Some(RayHit(ray, solid, hit.get))
    closestHit
