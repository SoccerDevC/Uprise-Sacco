<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Loan_Requests extends Model
{
    use HasFactory;
    protected $table = 'loan_requests';

    protected $fillable = ['loan_approval_status'];

    protected $primaryKey = 'loan_application_number';
    public $incrementing = false;

}
